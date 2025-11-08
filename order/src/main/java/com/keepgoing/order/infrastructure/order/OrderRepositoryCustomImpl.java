package com.keepgoing.order.infrastructure.order;



import static com.keepgoing.order.domain.order.QOrder.*;

import com.keepgoing.order.domain.order.OrderState;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
public class OrderRepositoryCustomImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public Page<UUID> findPendingIds(Collection<OrderState> states, Pageable pageable) {

        List<UUID> content = queryFactory
            .select(order.id)
            .from(order)
            .where(order.orderState.in(states))
            .orderBy(order.createdAt.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(order.count())
            .from(order)
            .where(order.orderState.in(states));
        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    public int claim(UUID orderId, OrderState beforeState, OrderState afterState) {
        long updated = queryFactory
            .update(order)
            .set(order.orderState, afterState)
            .where(
                order.id.eq(orderId),
                order.orderState.eq(beforeState)
            )
            .execute();

        em.flush(); em.clear();

        return (int) updated;
    }

    @Override
    public int updateOrderStateToProductVerifiedWithHub(UUID orderId, UUID hubId) {
        long updated = queryFactory
            .update(order)
            .set(order.orderState, OrderState.PRODUCT_VERIFIED)
            .set(order.hubId, hubId)
            .where(
                order.id.eq(orderId),
                order.orderState.eq(OrderState.PRODUCT_VALIDATION_IN_PROGRESS)
            )
            .execute();
        em.flush(); em.clear();

        return (int) updated;

    }

    @Override
    public int updateOrderStateToAwaitingPayment(UUID orderId) {
        long updated = queryFactory
            .update(order)
            .set(order.orderState, OrderState.AWAITING_PAYMENT)
            .where(
                order.id.eq(orderId),
                order.orderState.eq(OrderState.STOCK_RESERVATION_IN_PROGRESS)
            )
            .execute();
        em.flush(); em.clear();

        return (int) updated;
    }

    @Override
    public int updateOrderStateToPaid(UUID orderId) {
        long updated = queryFactory
            .update(order)
            .set(order.orderState, OrderState.PAID)
            .where(
                order.id.eq(orderId),
                order.orderState.eq(OrderState.PAYMENT_IN_PROGRESS)
            )
            .execute();
        em.flush(); em.clear();

        return (int) updated;
    }

    @Override
    public int updateOrderStateToCompleted(UUID orderId) {
        long updated = queryFactory
            .update(order)
            .set(order.orderState, OrderState.COMPLETED)
            .where(
                order.id.eq(orderId),
                order.orderState.eq(OrderState.COMPLETION_IN_PROGRESS)
            )
            .execute();
        em.flush(); em.clear();

        return (int) updated;
    }
}
