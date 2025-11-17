package com.sparta.practiceorder.order.repository;

import com.sparta.practiceorder.order.domain.Order;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID> {

}
