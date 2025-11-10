package com.keepgoing.order.infrastructure.outbox;

import com.keepgoing.order.domain.outbox.OutBoxState;
import java.time.LocalDateTime;
import java.util.Set;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderOutboxRepositoryCustom {

    // Query
    Page<Long> findPendingOutboxIds(Set<OutBoxState> states, Pageable pageable);

    // Command
    int claim(Long outboxId, OutBoxState beforeState, OutBoxState afterState, LocalDateTime now);

    int updateStateToCompletedForDelivery(Long outboxId, LocalDateTime now);

    int updateStateToCompletedForNotification(Long outboxId, LocalDateTime now);

    int updateOrderStateToFailedForDelivery(Long outboxId, LocalDateTime now);

    int updateOrderStateToFailedForNotification(Long outboxId, LocalDateTime now);



}
