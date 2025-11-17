package com.keepgoing.order.application.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keepgoing.order.application.dto.CreateOrderCommand;
import com.keepgoing.order.application.dto.CreateOrderPayloadForDelivery;
import com.keepgoing.order.application.dto.CreateOrderPayloadForNotification;
import com.keepgoing.order.application.event.order.OrderCreatedEvent;
import com.keepgoing.order.application.exception.DeleteOrderFailException;
import com.keepgoing.order.application.exception.InvalidOrderStateException;
import com.keepgoing.order.application.exception.NotFoundOrderException;
import com.keepgoing.order.application.exception.OrderCancelFailException;
import com.keepgoing.order.domain.order.CancelState;
import com.keepgoing.order.domain.order.PaymentApplyResult;
import com.keepgoing.order.domain.outbox.AggregateType;
import com.keepgoing.order.domain.outbox.EventType;
import com.keepgoing.order.domain.order.Order;
import com.keepgoing.order.domain.outbox.OrderOutbox;
import com.keepgoing.order.domain.order.OrderState;
import com.keepgoing.order.domain.outbox.OutBoxState;
import com.keepgoing.order.infrastructure.outbox.OrderOutboxRepository;
import com.keepgoing.order.infrastructure.order.OrderRepository;
import com.keepgoing.order.presentation.dto.response.api.CancelOrderResponse;
import com.keepgoing.order.presentation.dto.response.api.CreateOrderResponse;
import com.keepgoing.order.presentation.dto.response.api.DeleteOrderInfo;
import com.keepgoing.order.presentation.dto.response.api.OrderInfo;
import com.keepgoing.order.presentation.dto.response.api.OrderStateInfo;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j(topic = "OrderService")
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderOutboxRepository orderOutboxRepository;
    private final ObjectMapper objectMapper;
    private final Clock clock;
    private final ApplicationEventPublisher applicationEventPublisher;


    @Transactional
    public CreateOrderResponse create(CreateOrderCommand createOrderCommand) {

        Order order = createOrderCommand.toEntity();
        Order savedOrder = orderRepository.save(order);

        applicationEventPublisher.publishEvent(OrderCreatedEvent.from(savedOrder));

        log.info("주문 생성 완료");
        return CreateOrderResponse.create(
            order.getId(),
            order.getMemberId(),
            order.getOrderState(),
            order.getOrderedAt()
        );
    }

    public Page<OrderInfo> getOrderPage(Pageable pageable) {
        return orderRepository.searchOrderPage(pageable).map(OrderInfo::from);
    }

    public Order findById(UUID orderId) {
        return orderRepository.findById(orderId)
            .orElse(null);
    }

    public OrderInfo searchOrderOne(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new NotFoundOrderException("주문을 찾을 수 없습니다.")
        );

        return OrderInfo.from(order);
    }

    @Transactional
    public DeleteOrderInfo deleteOrder(UUID orderId, Long memberId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new NotFoundOrderException("주문을 찾을 수 없습니다. : 주문 삭제 이전")
        );

        if (order.getOrderState() != OrderState.ORDER_CONFIRMED) {
            throw new InvalidOrderStateException("주문 확정 상태에서만 주문을 삭제할 수 있습니다.");
        }

        LocalDateTime now = LocalDateTime.now(clock);

        int deleted = orderRepository.deleteOrder(orderId, memberId, now, order.getVersion());

        if (deleted == 0) {
            throw new DeleteOrderFailException("주문 삭제에 실패했습니다.");
        }

        return DeleteOrderInfo.create(
            orderId,
            memberId,
            now
        );
    }

    public OrderStateInfo findOrderState(UUID orderId) {
        OrderState state = orderRepository.findOrderStateById(orderId)
            .orElseThrow(
                () -> new NotFoundOrderException("해당 주문을 찾을 수 없습니다.")
            );

        return OrderStateInfo.create(orderId, state);
    }

    @Transactional
    public int claim(UUID orderId, OrderState beforeState, OrderState afterState) {
        return orderRepository.claim(orderId, beforeState, afterState, LocalDateTime.now(clock));
    }

    @Transactional
    public void toProductVerified(UUID orderId, UUID hubId) {
        int u = orderRepository.updateOrderStateToProductVerifiedWithHub(orderId, hubId, LocalDateTime.now(clock));
        if (u == 0) throw new IllegalStateException("전이 실패(버전/상태 불일치): " + orderId);
    }

    @Transactional
    public void toAwaitingPayment(UUID orderId) {
        int u = orderRepository.updateOrderStateToAwaitingPayment(orderId, LocalDateTime.now(clock));
        if (u == 0) throw new IllegalStateException("전이 실패: " + orderId);
    }

    @Transactional
    public void toPaid(UUID orderId) {
        // Outbox 페이로드가 필요하면, 전이 전/후에 필요한 필드만 projection으로 읽거나
        // 기존 order를 읽어와도 됨(외부 I/O는 없음)
        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new NotFoundOrderException("해당 주문을 찾을 수 없습니다.")
        ); // 읽기용

        int result = orderRepository.updateOrderStateToPaid(orderId, LocalDateTime.now(clock));

        if (result == 1) {
            Order fresh = orderRepository.findById(orderId).orElseThrow();
            String payloadForNotification = makePayloadForNotification(fresh);
            orderOutboxRepository.save(OrderOutbox.create(
                UUID.randomUUID(),
                AggregateType.ORDER,
                fresh.getId().toString(),
                EventType.PAID,
                OutBoxState.NOTIFICATION_PENDING,
                payloadForNotification,
                LocalDateTime.now(clock)
            ));
        }

    }

    @Transactional
    public void toCompleted(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(); // 읽기용

        int u = orderRepository.updateOrderStateToCompleted(orderId, LocalDateTime.now(clock));
        if (u == 0) throw new IllegalStateException("전이 실패: " + orderId);

        Order fresh = orderRepository.findById(orderId).orElseThrow();
        String payloadForDelivery = makePayloadForDelivery(fresh);
        orderOutboxRepository.save(OrderOutbox.create(
            UUID.randomUUID(),
            AggregateType.ORDER,
            fresh.getId().toString(),
            EventType.COMPLETED,
            OutBoxState.DELIVERY_PENDING,
            payloadForDelivery,
            LocalDateTime.now(clock)
        ));

    }

    @Transactional
    public void toFail(UUID orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow();
        if (order.getOrderState() == OrderState.FAILED) return;
        order.changeOrderStateToFail();
    }

    @Transactional
    public PaymentApplyResult updateStateToPaid(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new NotFoundOrderException("주문을 찾을 수 없습니다. : 상태 변경 이전(결제)")
        );

        if (order.isCancellationInProgress()) {
            return PaymentApplyResult.CANCELLED;
        }

        if (order.isPaid()) {
            return PaymentApplyResult.ALREADY_PAID;
        }

        int updated = orderRepository.updateOrderStateToPaidForPayment(
            orderId,
            order.getVersion(),
            LocalDateTime.now(clock)
        );

        if (updated == 1) {
            Order fresh = orderRepository.findById(orderId).orElseThrow();
            String payloadForNotification = makePayloadForNotification(fresh);
            orderOutboxRepository.save(OrderOutbox.create(
                UUID.randomUUID(),
                AggregateType.ORDER,
                fresh.getId().toString(),
                EventType.PAID,
                OutBoxState.NOTIFICATION_PENDING,
                payloadForNotification,
                LocalDateTime.now(clock)
            ));

            return PaymentApplyResult.APPLIED;
        }

        Order check = orderRepository.findById(orderId).orElseThrow();

        if (check.isPaid()) {
            return PaymentApplyResult.ALREADY_PAID;
        }

        if (check.isCancellationInProgress()) {
            return PaymentApplyResult.CANCELLED;
        }

        // AWAITING_PAYMENT인데 갱신 실패, 오류 발생
        // TODO: 나중에 해당 케이스에 대한 후속 처리가 필요해 보임
        return PaymentApplyResult.ALREADY_PAID;
    }

    // 취소 요청이 들어오면 취소 예약하는 서비스
    @Transactional
    public CancelOrderResponse updateCancelStateCancelRequired(UUID orderId, Long memberId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
            () -> new NotFoundOrderException("주문을 찾을 수 없습니다.")
        );

        LocalDateTime now = LocalDateTime.now(clock);

        // 이미 처리된 작업
        if (order.isCancellationInProgress()) {
            return CancelOrderResponse.fail(
                orderId,
                memberId,
                now,
                "이미 취소 요청(진행) 중입니다."
                );
        }

        int updated = orderRepository.updateCancelRequired(
            orderId,
            order.getOrderState(),
            now
        );

        if (updated == 0) {
            Order current = orderRepository.findById(orderId).orElseThrow(
                () -> new NotFoundOrderException("주문을 찾을 수 없습니다.")
            );
            if (current.isCancellationInProgress()) {
                return CancelOrderResponse.fail(
                    orderId,
                    memberId,
                    now,
                    "이미 취소 요청(진행) 중입니다."
                );
            }
            throw new OrderCancelFailException("주문 취소 요청에 실패했습니다.");
        }

        return CancelOrderResponse.success(
            orderId,
            memberId,
            now,
            "주문 취소가 접수되었습니다."
        );
    }

    @Transactional
    public int cancelClaim(UUID orderId, Collection<OrderState> orderStates , CancelState beforeCancelState, CancelState afterCancelState) {
        return orderRepository.cancelClaim(
            orderId,
            orderStates,
            beforeCancelState,
            afterCancelState,
            LocalDateTime.now(clock)
        );
    }

    @Transactional
    public int updateCancelStateToOrderCancelled(UUID orderId) {
        return orderRepository.updateCancelStateToOrderCancelled(orderId, LocalDateTime.now(clock));
    }

    @Transactional
    public int updateCancelStateToOrderCancelAwaiting(UUID orderId) {
        return orderRepository.updateCancelStateToOrderCancelAwaiting(orderId, LocalDateTime.now(clock));
    }

    @Transactional
    public int updateCancelStateToInventoryReservationCancelAwaiting(UUID orderId) {
        return orderRepository.updateCancelStateToInventoryReservationCancelAwaiting(orderId, LocalDateTime.now(clock));
    }

    @Transactional
    public int revertPaymentCancelToAwaiting(UUID orderId) {
        return orderRepository.revertPaymentCancelToAwaiting(orderId, LocalDateTime.now(clock));
    }

    @Transactional
    public int revertInventoryReservationCancelToAwaiting(UUID orderId) {
        return orderRepository.revertInventoryReservationCancelToAwaiting(orderId, LocalDateTime.now(clock));
    }



    private String makePayloadForDelivery(Order order) {
        CreateOrderPayloadForDelivery createOrderPayloadForDelivery = new CreateOrderPayloadForDelivery(
            order.getId(),
            order.getReceiverId(),
            order.getProductId(),
            order.getOrderState()
        );

        try {
            return objectMapper.writeValueAsString(createOrderPayloadForDelivery);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON으로 변환하지 못했습니다.");
        }
    }

    private String makePayloadForNotification(Order order) {
        CreateOrderPayloadForNotification createOrderPayloadForNotification = new CreateOrderPayloadForNotification(
            order.getId(),
            order.getSupplierName(),
            order.getReceiverName(),
            order.getProductName(),
            order.getQuantity(),
            String.valueOf(order.getOrderedAt()),
            String.valueOf(order.getDeliveryDueAt()),
            order.getDeliveryRequestNote()
        );

        try {
            return objectMapper.writeValueAsString(createOrderPayloadForNotification);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON으로 변환하지 못했습니다.");
        }
    }
}