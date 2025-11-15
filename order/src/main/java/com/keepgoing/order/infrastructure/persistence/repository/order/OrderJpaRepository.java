package com.keepgoing.order.infrastructure.persistence.repository.order;

import com.keepgoing.order.infrastructure.persistence.order.entity.OrderEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<OrderEntity, UUID>,
    OrderJpaRepositoryCustom {


}
