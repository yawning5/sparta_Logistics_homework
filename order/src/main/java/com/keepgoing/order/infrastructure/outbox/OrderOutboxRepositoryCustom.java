package com.keepgoing.order.infrastructure.outbox;

import com.keepgoing.order.domain.outbox.OutBoxState;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderOutboxRepositoryCustom {

    Page<Long> findPendingOutboxIds(Set<OutBoxState> states, Pageable pageable);

    int updateStateToCompletedForDelivery(Long outboxId);

    int updateStateToCompletedForNotification(Long outboxId);

    int updateOrderStateToFailedForDelivery(Long outboxId);

    int updateOrderStateToFailedForNotification(Long outboxId);

    int claim(Long outboxId, OutBoxState beforeState, OutBoxState afterState);

}
