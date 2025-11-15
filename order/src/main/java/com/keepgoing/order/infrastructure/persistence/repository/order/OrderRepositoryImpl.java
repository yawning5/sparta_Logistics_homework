package com.keepgoing.order.infrastructure.persistence.repository.order;

import com.keepgoing.order.application.repository.OrderRepository;
import com.keepgoing.order.domain.model.Order;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl implements OrderRepository {

    private OrderJpaRepository orderJpaRepository;

    @Override
    public Order save(Order order) {
        return null;
    }
}
