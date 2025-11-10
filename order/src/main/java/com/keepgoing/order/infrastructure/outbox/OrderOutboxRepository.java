package com.keepgoing.order.infrastructure.outbox;

import com.keepgoing.order.domain.outbox.OrderOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderOutboxRepository extends JpaRepository<OrderOutbox, Long>, OrderOutboxRepositoryCustom {

}
