package com.keepgoing.order.application.repository;

import com.keepgoing.order.domain.model.Order;
import java.util.UUID;

public interface OrderRepository {

    Order findById(UUID orderId);

    Order save(Order order);

}
