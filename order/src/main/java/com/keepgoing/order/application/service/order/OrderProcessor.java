package com.keepgoing.order.application.service.order;

import com.keepgoing.order.application.exception.InventoryReservationFailException;
import com.keepgoing.order.application.exception.NotFoundOrderException;
import com.keepgoing.order.application.exception.NotFoundProductException;

import com.keepgoing.order.domain.order.Order;
import com.keepgoing.order.domain.order.OrderState;
import com.keepgoing.order.presentation.client.HubClient;
import com.keepgoing.order.presentation.client.ProductClient;
import com.keepgoing.order.presentation.dto.request.ReservationInventoryRequest;
import com.keepgoing.order.presentation.dto.response.client.InventoryReservationResponse;
import com.keepgoing.order.presentation.dto.response.client.ProductInfo;

import com.keepgoing.order.presentation.dto.response.client.ProductSearchResponse;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Slf4j(topic = "OrderProcessor")
@Service
@RequiredArgsConstructor
public class OrderProcessor {

    private final OrderService orderService;
    private final ProductClient productClient;
    private final HubClient hubClient;

    @Async("taskExecutor")
    public void processTask(UUID orderId) {
        Order order = orderService.findById(orderId);

        if (order == null) {
            log.error("주문을 찾지 못함 {}", orderId);
            return;
        }

        try {
            switch (order.getOrderState()) {
                case PENDING_VALIDATION :
                    log.info("상품 유효성 검증 수행 {}", orderId);

                    int updateResult1 = orderService.claim(
                        orderId,
                        OrderState.PENDING_VALIDATION,
                        OrderState.PRODUCT_VALIDATION_IN_PROGRESS
                    );

                    if (updateResult1 == 0) {
                        log.error("이미 작업을 진행 중이거나 취소 작업 진행중 {}", orderId);
                        return;
                    }

                    Order orderForProductValidation = orderService.findById(orderId);

                    if (orderForProductValidation == null) {
                        orderService.toFail(orderId);
                        log.error("[상품 검증 실패] 검증 중 주문이 존재하지 않음 orderId={}", orderId);
                        return;
                    }

                    UUID productId = orderForProductValidation.getProductId();
                    ProductSearchResponse productResponse = null;
                    try {
                         productResponse = productClient.getProduct(productId);

                        if (productResponse == null || productResponse.fail()) {
                            log.error("[상품 검증 실패] ProductClient 호출 null 혹은 실패 orderId={}", orderId);
                            orderService.toFail(orderId);
                            return;
                        }

                    } catch (Exception e) {
                        log.error("[상품 검증 실패] ProductClient 호출 예외 발생 orderId={}, msg={}", orderId, e.getMessage(), e);
                        orderService.toFail(orderId);
                        return;
                    }
                    ProductInfo productInfo = productResponse.getProductInfo();

                    if (productInfo == null || productInfo.getHubId() == null){
                        log.error("[상품 검증 실패] ProductClient 응답이 null orderId={}", orderId);
                        orderService.toFail(orderId);
                        return;
                    }

                    UUID hubId = null;
                    try {
                        hubId = productInfo.getHubId();
                    } catch (IllegalArgumentException ex) {
                        log.error("[상품 검증 실패] 잘못된 hubId 포맷: {} orderId={}", productInfo.getHubId(), orderId);
                        orderService.toFail(orderId);
                        return;
                    }

                    orderService.toProductVerified(orderId, hubId);
                    break;

                case PRODUCT_VERIFIED :
                    log.info("재고 예약 수행 {}", orderId);

                    int updateResult2 = orderService.claim(
                        orderId,
                        OrderState.PRODUCT_VERIFIED,
                        OrderState.STOCK_RESERVATION_IN_PROGRESS
                    );

                    if (updateResult2 == 0) {
                        log.error("이미 작업을 진행 중이거나 취소 작업 진행중 {}", orderId);
                        return;
                    }

                    Order orderForInventoryReservation = orderService.findById(orderId);

                    if (orderForInventoryReservation == null) {
                        log.error("[재고 예약 실패] 주문을 찾지 못함 orderId={}", orderId);
                        orderService.toFail(orderId);
                        return;
                    }

                    String productIdForInventory = String.valueOf(orderForInventoryReservation.getProductId());
                    String hubIdForInventory = orderForInventoryReservation.getHubId().toString();
                    Integer quantity = orderForInventoryReservation.getQuantity();
                    String idempotencyKey = orderForInventoryReservation.getIdempotencyKey().toString();

                    InventoryReservationResponse inventoryResponse = null;
                    try {
                        inventoryResponse = hubClient.reservationInventoryForProduct(
                            ReservationInventoryRequest.create(productIdForInventory, hubIdForInventory, quantity)
                        );

                        if (inventoryResponse == null || inventoryResponse.fail()) {
                            log.error("[재고 예약 실패] HubClient null orderId={}", orderId);
                            orderService.toFail(orderId);
                            return;
                        }
                    } catch (Exception e) {
                        log.error("[재고 예약 실패] HubClient 예외 발생 orderId={}, msg={}", orderId, e.getMessage(), e);
                        orderService.toFail(orderId);
                        return;
                    }

                    orderService.toAwaitingPayment(orderId);

                    break;
                case PAID :
                    log.info("배송을 위한 Outbox에 데이터 등록 {}", orderId);

                    int updateResult4 = orderService.claim(
                        orderId,
                        OrderState.PAID,
                        OrderState.COMPLETION_IN_PROGRESS
                    );

                    if (updateResult4 == 0) {
                        log.error("이미 작업을 진행 중이거나 취소 작업 진행중 {}", orderId);
                        return;
                    }

                    orderService.toCompleted(orderId);
                    break;

                default:
                    log.warn("처리 불필요/미지원 상태: {} {}", orderId, order.getOrderState());
                    return;
            }

        } catch (NotFoundProductException | NotFoundOrderException | InventoryReservationFailException orderFailException) {
            log.error("주문을 처리하는 도중 문제 발생 {}", orderId, orderFailException);

        } catch (Exception e) {
            log.error("주문을 처리 실패 {}", orderId, e);
        }
    }

}