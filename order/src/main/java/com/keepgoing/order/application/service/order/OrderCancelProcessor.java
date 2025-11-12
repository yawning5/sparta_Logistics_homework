package com.keepgoing.order.application.service.order;

import com.keepgoing.order.domain.order.CancelState;
import com.keepgoing.order.domain.order.Order;
import com.keepgoing.order.domain.order.OrderState;
import com.keepgoing.order.presentation.client.HubClient;
import com.keepgoing.order.presentation.dto.request.ReservationCancelInventoryRequest;
import com.keepgoing.order.presentation.dto.response.client.InventoryReservationResponse;
import java.util.Collection;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j(topic = "OrderCancelProcessor")
@Service
@RequiredArgsConstructor
public class OrderCancelProcessor {

    private final OrderService orderService;
    private final HubClient hubClient;

    public void processTask(UUID orderId, Collection<OrderState> orderStates) {
        try {
            Order order = orderService.findById(orderId);

            if (order == null) {
                log.error("주문을 찾지 못함 {}", orderId);
                return;
            }

            switch (order.getCancelState()) {
                case ORDER_CANCEL_AWAITING -> {

                    int updated = orderService.cancelClaim(
                        orderId,
                        orderStates,
                        CancelState.ORDER_CANCEL_AWAITING,
                        CancelState.ORDER_CANCEL_IN_PROGRESS
                    );

                    if (updated == 0) {
                        log.error("이미 작업을 진행 중이거나 상태가 일치하지 않습니다.");
                        return;
                    }

                    Order refresh = orderService.findById(orderId);

                    if (refresh == null) {
                        log.error("주문 취소 실패 {}", orderId);
                        return;
                    }

                    orderService.updateCancelStateToOrderCancelled(orderId);
                }

                case INVENTORY_RESERVATION_CANCEL_AWAITING -> {

                    int updated = orderService.cancelClaim(
                        orderId,
                        orderStates,
                        CancelState.INVENTORY_RESERVATION_CANCEL_AWAITING,
                        CancelState.INVENTORY_RESERVATION_CANCEL_IN_PROGRESS
                    );

                    if (updated == 0) {
                        log.error("이미 작업을 진행 중이거나 상태가 일치하지 않습니다.");
                        return;
                    }

                    Order refresh = orderService.findById(orderId);

                    if (refresh == null) {
                        log.error("재고 예약 취소 실패 {}", orderId);
                        orderService.revertInventoryReservationCancelToAwaiting(orderId);
                        return;
                    }

                    try {
                        InventoryReservationResponse response = hubClient.reservationCancelInventoryForProduct(
                            ReservationCancelInventoryRequest.create(
                                refresh.getProductId(),
                                refresh.getHubId(),
                                refresh.getQuantity())
                        );

                        if (response == null || response.fail()) {
                            log.error("재고 예약 취소 실패 {}", orderId);
                            orderService.revertInventoryReservationCancelToAwaiting(orderId);
                            return;
                        }

                    } catch (Exception e) {
                        log.error("재고 예약 취소 실패 {}", orderId);
                        orderService.revertInventoryReservationCancelToAwaiting(orderId);
                        return;
                    }

                    orderService.updateCancelStateToOrderCancelAwaiting(orderId);
                }

                case PAYMENT_CANCEL_AWAITING -> {
                    int updated = orderService.cancelClaim(
                        orderId,
                        orderStates,
                        CancelState.PAYMENT_CANCEL_AWAITING,
                        CancelState.PAYMENT_CANCEL_IN_PROGRESS
                    );

                    if (updated == 0) {
                        log.error("이미 작업을 진행 중이거나 상태가 일치하지 않습니다.");
                        return;
                    }

                    Order refresh = orderService.findById(orderId);

                    if (refresh == null) {
                        orderService.revertPaymentCancelToAwaiting(orderId);
                        log.error("결제 취소 실패 {}", orderId);

                        return;
                    }

                    try {
                        // 결제 서비스로 취소 API 호출해야 함
                        // 결제 보낸 것으로 가정
                        // TODO: 결제 취소 API 호출을 수행하는 경우 실패에 대한 예외 처리 해줘야 함(상태전이)

                    } catch (Exception e) {
                        log.error("결제 취소 실패 {}", orderId);
                        orderService.revertPaymentCancelToAwaiting(orderId);
                        return;
                    }

                    orderService.updateCancelStateToInventoryReservationCancelAwaiting(orderId);
                }

                default -> {
                    log.debug("취소 작업 대상 아님 orderId={}, cancelState={}", orderId, order.getCancelState());
                    return;
                }
            }
        } catch (Exception e) {
            log.error("주문을 취소하던 도중 문제가 발생했습니다. {}", orderId);
        }
    }
}
