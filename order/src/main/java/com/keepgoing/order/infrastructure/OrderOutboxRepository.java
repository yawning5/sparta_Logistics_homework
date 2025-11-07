package com.keepgoing.order.infrastructure;

import com.keepgoing.order.domain.OrderOutbox;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderOutboxRepository extends JpaRepository<OrderOutbox, Long>, OrderOutboxRepositoryCustom {

}
