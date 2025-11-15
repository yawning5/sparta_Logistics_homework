package com.keepgoing.order.infrastructure.persistence.repository.order;

import com.keepgoing.order.application.repository.OrderRepository;
import com.keepgoing.order.domain.model.Order;
import com.keepgoing.order.infrastructure.persistence.mapper.OrderMapper;
import com.keepgoing.order.infrastructure.persistence.order.entity.OrderEntity;
import com.keepgoing.order.presentation.exception.NotFoundOrderException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order findById(UUID orderId) {

        OrderEntity orderEntity = orderJpaRepository.findById(orderId).orElseThrow(
            () -> new NotFoundOrderException("아이디에 해당하는 주문을 찾을 수 없습니다.")
        );

        return OrderMapper.toDomain(orderEntity);
    }

    @Override
    public Order save(Order order) {

        OrderEntity orderEntity = OrderMapper.toEntityForCreate(order);

        orderEntity = orderJpaRepository.save(orderEntity);

        return OrderMapper.toDomain(orderEntity);
    }
}
