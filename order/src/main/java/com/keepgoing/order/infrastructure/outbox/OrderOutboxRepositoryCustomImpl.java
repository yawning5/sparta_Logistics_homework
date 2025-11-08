package com.keepgoing.order.infrastructure.outbox;

import static com.keepgoing.order.domain.outbox.QOrderOutbox.*;

import com.keepgoing.order.domain.outbox.OutBoxState;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class OrderOutboxRepositoryCustomImpl implements OrderOutboxRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

    @Override
    public Page<Long> findPendingOutboxIds(Set<OutBoxState> states, Pageable pageable) {

        List<Long> content = queryFactory
            .select(orderOutbox.id)
            .from(orderOutbox)
            .where(orderOutbox.state.in(states))
            .orderBy(orderOutbox.createdAt.asc())
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(orderOutbox.count())
            .from(orderOutbox)
            .where(orderOutbox.state.in(states));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    @Override
    @Transactional
    public int updateStateToCompletedForDelivery(Long outboxId, LocalDateTime now) {

        long updated = queryFactory
            .update(orderOutbox)
            .set(orderOutbox.state, OutBoxState.DELIVERY_COMPLETED)
            .set(orderOutbox.updatedAt, now)
            .where(
                orderOutbox.id.eq(outboxId),
                orderOutbox.state.eq(OutBoxState.DELIVERY_COMPLETION_IN_PROGRESS)
            )
            .execute();

        em.flush(); em.clear();

        return (int) updated;
    }

    @Override
    @Transactional
    public int updateStateToCompletedForNotification(Long outboxId, LocalDateTime now) {

        long updated = queryFactory
            .update(orderOutbox)
            .set(orderOutbox.state, OutBoxState.NOTIFICATION_COMPLETED)
            .set(orderOutbox.updatedAt, now)
            .where(
                orderOutbox.id.eq(outboxId),
                orderOutbox.state.eq(OutBoxState.NOTIFICATION_COMPLETION_IN_PROGRESS)
            )
            .execute();

        em.flush(); em.clear();

        return (int) updated;
    }

    @Override
    @Transactional
    public int updateOrderStateToFailedForDelivery(Long outboxId, LocalDateTime now) {
        long updated = queryFactory
            .update(orderOutbox)
            .set(orderOutbox.state, OutBoxState.DELIVERY_FAILED)
            .set(orderOutbox.updatedAt, now)
            .where(
                orderOutbox.id.eq(outboxId),
                orderOutbox.state.eq(OutBoxState.DELIVERY_COMPLETION_IN_PROGRESS)
            )
            .execute();
        em.flush(); em.clear();

        return (int) updated;
    }

    @Override
    @Transactional
    public int updateOrderStateToFailedForNotification(Long outboxId, LocalDateTime now) {
        long updated = queryFactory
            .update(orderOutbox)
            .set(orderOutbox.state, OutBoxState.NOTIFICATION_FAILED)
            .set(orderOutbox.updatedAt, now)
            .where(
                orderOutbox.id.eq(outboxId),
                orderOutbox.state.eq(OutBoxState.NOTIFICATION_COMPLETION_IN_PROGRESS)
            )
            .execute();
        em.flush(); em.clear();

        return (int) updated;
    }

    @Override
    @Transactional
    public int claim(Long outboxId, OutBoxState beforeState, OutBoxState afterState, LocalDateTime now) {

        long updated = queryFactory
            .update(orderOutbox)
            .set(orderOutbox.state, afterState)
            .set(orderOutbox.updatedAt, now)
            .where(
                orderOutbox.id.eq(outboxId),
                orderOutbox.state.eq(beforeState)
            )
            .execute();

        em.flush(); em.clear();

        return (int) updated;
    }
}
