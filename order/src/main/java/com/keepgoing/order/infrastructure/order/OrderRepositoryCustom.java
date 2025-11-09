package com.keepgoing.order.infrastructure.order;

import com.keepgoing.order.domain.order.Order;
import com.keepgoing.order.domain.order.OrderState;
import java.time.LocalDateTime;
import java.util.Collection;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<UUID> findPendingIds(Collection<OrderState> states, Pageable pageable);

    int claim(UUID orderId, OrderState beforeState, OrderState afterState, LocalDateTime now);

    int updateOrderStateToProductVerifiedWithHub(UUID orderId, UUID hubId, LocalDateTime now);

    int updateOrderStateToAwaitingPayment(UUID orderId, LocalDateTime now);

    int updateOrderStateToPaid(UUID orderId, LocalDateTime now);

    int updateOrderStateToCompleted(UUID orderId, LocalDateTime now);

    Page<Order> searchOrderPage(Pageable pageable);

    Optional<OrderState> findOrderStateById(UUID orderId);

    int deleteOrder(UUID orderId, Long memberId, LocalDateTime now, Long version);
}
