package com.keepgoing.order.infrastructure.order;

import com.keepgoing.order.domain.order.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID>, OrderRepositoryCustom {

}
