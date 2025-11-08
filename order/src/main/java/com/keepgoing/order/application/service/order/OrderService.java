package com.keepgoing.order.application.service.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keepgoing.order.application.dto.CreateOrderCommand;
import com.keepgoing.order.application.dto.CreateOrderPayloadForDelivery;
import com.keepgoing.order.application.dto.CreateOrderPayloadForNotification;
import com.keepgoing.order.domain.outbox.AggregateType;
import com.keepgoing.order.domain.outbox.EventType;
import com.keepgoing.order.domain.order.Order;
import com.keepgoing.order.domain.outbox.OrderOutbox;
import com.keepgoing.order.domain.order.OrderState;
import com.keepgoing.order.domain.outbox.OutBoxState;
import com.keepgoing.order.infrastructure.outbox.OrderOutboxRepository;
import com.keepgoing.order.infrastructure.order.OrderRepository;
import com.keepgoing.order.presentation.dto.response.CreateOrderResponse;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
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


    @Transactional
    public CreateOrderResponse create(CreateOrderCommand createOrderCommand) {

        Order order = createOrderCommand.toEntity();
        orderRepository.save(order);

        return CreateOrderResponse.create(
            order.getId(),
            order.getOrderState(),
            order.getOrderedAt()
        );
    }

    public Order findById(UUID orderId) {
        return orderRepository.findById(orderId)
            .orElse(null);
    }

    @Transactional
    public void toProductVerified(UUID orderId, UUID hubId) {
        int u = orderRepository.updateOrderStateToProductVerifiedWithHub(orderId, hubId);
        if (u == 0) throw new IllegalStateException("전이 실패(버전/상태 불일치): " + orderId);
    }

    @Transactional
    public void toAwaitingPayment(UUID orderId) {
        int u = orderRepository.updateOrderStateToAwaitingPayment(orderId);
        if (u == 0) throw new IllegalStateException("전이 실패: " + orderId);
    }

    @Transactional
    public void toPaid(UUID orderId) {
        // Outbox 페이로드가 필요하면, 전이 전/후에 필요한 필드만 projection으로 읽거나
        // 기존 order를 읽어와도 됨(외부 I/O는 없음)
        Order order = orderRepository.findById(orderId).orElseThrow(); // 읽기용

        String payloadForNotification = makePayloadForNotification(order);
        orderOutboxRepository.save(OrderOutbox.create(
            UUID.randomUUID(),
            AggregateType.ORDER,
            order.getId().toString(),
            EventType.PAID,
            OutBoxState.NOTIFICATION_PENDING,
            payloadForNotification,
            LocalDateTime.now(clock)
        ));

        int u = orderRepository.updateOrderStateToPaid(orderId);
        if (u == 0) throw new IllegalStateException("전이 실패: " + orderId);
    }

    @Transactional
    public void toCompleted(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(); // 읽기용

        String payloadForDelivery = makePayloadForDelivery(order);
        orderOutboxRepository.save(OrderOutbox.create(
            UUID.randomUUID(),
            AggregateType.ORDER,
            order.getId().toString(),
            EventType.COMPLETED,
            OutBoxState.DELIVERY_PENDING,
            payloadForDelivery,
            LocalDateTime.now(clock)
        ));

        int u = orderRepository.updateOrderStateToCompleted(orderId);
        if (u == 0) throw new IllegalStateException("전이 실패: " + orderId);
    }

    @Transactional
    public void toFail(UUID orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow();
        if (order.getOrderState() == OrderState.FAILED) return;
        order.changeOrderStateToFail();

        // TODO: 실패 후 후속처리 필요 (Outbox 혹은 다른 방식 적용)
    }

    @Transactional
    public int claim(UUID orderId, OrderState beforeState, OrderState afterState) {
        return orderRepository.claim(orderId, beforeState, afterState);
    }

    private String makePayloadForNotification(Order order) {
        CreateOrderPayloadForNotification createOrderPayloadForNotification = new CreateOrderPayloadForNotification(
            order.getId(),
            order.getSupplierName(),
            order.getReceiverName(),
            order.getProductName(),
            order.getQuantity(),
            order.getOrderedAt(),
            order.getDeliveryDueAt(),
            order.getDeliveryRequestNote()
        );

        try {
            return objectMapper.writeValueAsString(createOrderPayloadForNotification);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("JSON으로 변환하지 못했습니다.");
        }
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
}