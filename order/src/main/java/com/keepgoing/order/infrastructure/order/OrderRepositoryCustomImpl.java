package com.keepgoing.order.infrastructure.order;



import static com.keepgoing.order.domain.order.QOrder.*;

import com.keepgoing.order.domain.order.Order;
import com.keepgoing.order.domain.order.OrderState;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.ComparableExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public int claim(UUID orderId, OrderState beforeState, OrderState afterState, LocalDateTime now) {
        long updated = queryFactory
            .update(order)
            .set(order.orderState, afterState)
            .set(order.updatedAt, now)
            .where(
                order.id.eq(orderId),
                order.orderState.eq(beforeState)
            )
            .execute();

        em.flush(); em.clear();

        return (int) updated;
    }

    @Override
    public int updateOrderStateToProductVerifiedWithHub(UUID orderId, UUID hubId, LocalDateTime now) {
        long updated = queryFactory
            .update(order)
            .set(order.orderState, OrderState.PRODUCT_VERIFIED)
            .set(order.hubId, hubId)
            .set(order.updatedAt, now)
            .where(
                order.id.eq(orderId),
                order.orderState.eq(OrderState.PRODUCT_VALIDATION_IN_PROGRESS)
            )
            .execute();
        em.flush(); em.clear();

        return (int) updated;

    }

    @Override
    public int updateOrderStateToAwaitingPayment(UUID orderId, LocalDateTime now) {
        long updated = queryFactory
            .update(order)
            .set(order.orderState, OrderState.AWAITING_PAYMENT)
            .set(order.updatedAt, now)
            .where(
                order.id.eq(orderId),
                order.orderState.eq(OrderState.STOCK_RESERVATION_IN_PROGRESS)
            )
            .execute();
        em.flush(); em.clear();

        return (int) updated;
    }

    @Override
    public int updateOrderStateToPaid(UUID orderId, LocalDateTime now) {
        long updated = queryFactory
            .update(order)
            .set(order.orderState, OrderState.PAID)
            .set(order.updatedAt, now)
            .where(
                order.id.eq(orderId),
                order.orderState.eq(OrderState.PAYMENT_IN_PROGRESS)
            )
            .execute();
        em.flush(); em.clear();

        return (int) updated;
    }

    @Override
    public int updateOrderStateToCompleted(UUID orderId, LocalDateTime now) {
        long updated = queryFactory
            .update(order)
            .set(order.orderState, OrderState.COMPLETED)
            .set(order.updatedAt, now)
            .where(
                order.id.eq(orderId),
                order.orderState.eq(OrderState.COMPLETION_IN_PROGRESS)
            )
            .execute();
        em.flush(); em.clear();

        return (int) updated;
    }

    @Override
    public Page<Order> searchOrderPage(Pageable pageable) {

        List<OrderSpecifier> orderSpecifiers = getOrderSpecifiers(pageable);

        List<Order> content = queryFactory
            .select(order)
            .from(order)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(orderSpecifiers.toArray(new OrderSpecifier[0]))
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(order.count())
            .from(order);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    // 동적 정렬을 위한 메서드
    private static List<OrderSpecifier> getOrderSpecifiers(Pageable pageable) {
        List<OrderSpecifier> orderSpecifiers = new ArrayList<>();

        for (Sort.Order o : pageable.getSort()) {

            // isAscending는 오름차순이면 true, 내림차순이면 false 반환
            com.querydsl.core.types.Order direction = o.isAscending() ?
                com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC;

            String property = o.getProperty();

            PathBuilder<?> path = new PathBuilder<>(Order.class, order.getMetadata());

            ComparableExpression<?> expr =
                path.getComparable(o.getProperty(), Comparable.class);

            orderSpecifiers.add(new OrderSpecifier(direction, expr));
        }
        return orderSpecifiers;
    }

    @Override
    public Optional<OrderState> findOrderStateById(UUID orderId) {
        OrderState orderState = queryFactory
            .select(order.orderState)
            .from(order)
            .where(order.id.eq(orderId))
            .fetchOne();

        return Optional.ofNullable(orderState);
    }
}
