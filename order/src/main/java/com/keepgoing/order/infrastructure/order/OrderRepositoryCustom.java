package com.keepgoing.order.infrastructure.order;

import com.keepgoing.order.domain.order.OrderState;
import java.util.Collection;

import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderRepositoryCustom {
    Page<UUID> findPendingIds(Collection<OrderState> states, Pageable pageable);

    int claim(UUID orderId, OrderState beforeState, OrderState afterState);

    int updateOrderStateToProductVerifiedWithHub(UUID orderId, UUID hubId);

    int updateOrderStateToAwaitingPayment(UUID orderId);

    int updateOrderStateToPaid(UUID orderId);

    int updateOrderStateToCompleted(UUID orderId);
}
