//package com.keepgoing.order.application.service.order;
//
//import com.keepgoing.order.domain.order.CancelState;
//import com.keepgoing.order.domain.order.OrderState;
//import com.keepgoing.order.infrastructure.order.OrderRepository;
//import java.util.Set;
//import java.util.UUID;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//
//@Slf4j(topic = "OrderScheduler")
//@Service
//@RequiredArgsConstructor
//public class OrderScheduler {
//
//    private final OrderRepository orderRepository;
//    private final OrderProcessor orderProcessor;
//    private final OrderCancelProcessor orderCancelProcessor;
//    private final CancelRequestMapper cancelRequestMapper;
//
//    private static final Set<OrderState> PROCESSING_STATES = Set.of(
//        OrderState.PENDING_VALIDATION,
//        OrderState.PRODUCT_VERIFIED,
//        OrderState.PAID
//    );
//
//    private static final Set<OrderState> CANCEL_ORDER_STATES = Set.of(
//        OrderState.PRODUCT_VERIFIED,
//        OrderState.AWAITING_PAYMENT,
//        OrderState.PAID,
//        OrderState.COMPLETED
//    );
//
//    private static final Set<CancelState> CANCEL_PROCESSING_STATES = Set.of(
//        CancelState.ORDER_CANCEL_AWAITING,
//        CancelState.INVENTORY_RESERVATION_CANCEL_AWAITING,
//        CancelState.PAYMENT_CANCEL_AWAITING
//    );
//
//    @Scheduled(fixedDelay = 5000)
//    public void distributeTasks() {
//        int page = 0;
//        int pageSize = 100;
//
//        while (true) {
//            Page<UUID> taskIds = orderRepository.findPendingIds(
//                PROCESSING_STATES,
//                PageRequest.of(page, pageSize)
//            );
//
//            if (!taskIds.hasContent()) break;
//
//            for (UUID orderId : taskIds) {
//                try {
//                    orderProcessor.processTask(orderId);
//                } catch (Exception e) {
//                    log.error("task 스케줄러 작업 위임 실패 {}", orderId, e);
//                }
//            }
//
//            if (!taskIds.hasNext()) break;
//            page++;
//        }
//    }
//
//    @Scheduled(fixedDelay = 5000)
//    public void mapRequested() {
//        int page = 0;
//        int pageSize = 100;
//
//        while (true) {
//            Page<UUID> taskIds = orderRepository.findByCancelState(
//                CancelState.CANCEL_REQUESTED,
//                CANCEL_ORDER_STATES,
//                PageRequest.of(page, pageSize)
//            );
//
//            if (!taskIds.hasContent()) break;
//
//            for (UUID orderId : taskIds) {
//                try {
//                    cancelRequestMapper.mapRequestToAwaiting(orderId); // @Transactional
//                } catch (Exception e) {
//                    log.error("취소 매핑 위임 실패 {}", orderId, e);
//                }
//            }
//
//            if (!taskIds.hasNext()) break;
//            page++;
//        }
//    }
//
//    @Scheduled(fixedDelay = 5000)
//    public void cancelTask() {
//        int page = 0;
//        int pageSize = 100;
//
//        while (true) {
//            Page<UUID> taskIds = orderRepository.findPendingCancelIds(
//                CANCEL_PROCESSING_STATES,
//                CANCEL_ORDER_STATES,
//                PageRequest.of(page, pageSize)
//            );
//
//            if (!taskIds.hasContent()) break;
//
//            for (UUID orderId : taskIds) {
//                try {
//                    orderCancelProcessor.processTask(
//                        orderId,
//                        CANCEL_ORDER_STATES
//                    );
//                } catch (Exception e) {
//                    log.error("취소 작업 실패 {}", orderId, e);
//                }
//            }
//
//            if (!taskIds.hasNext()) break;
//            page++;
//        }
//    }
//
//}
