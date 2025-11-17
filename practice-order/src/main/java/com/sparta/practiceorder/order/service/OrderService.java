package com.sparta.practiceorder.order.service;

import com.sparta.practiceorder.common.EventPublisher;
import com.sparta.practiceorder.order.dto.OrderCreateRequest;
import com.sparta.practiceorder.order.dto.OrderResponse;
import com.sparta.practiceorder.order.domain.Order;
import com.sparta.practiceorder.order.event.OrderCreatedEvent;
import com.sparta.practiceorder.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public OrderResponse createOrder(OrderCreateRequest request) {


        Order order = Order.create(
            request.getProductId(),
            request.getSupplierId(),
            request.getReceiverId(),
            request.getQuantity()
        );
        Order savedOrder = orderRepository.save(order);

        eventPublisher.publish(OrderCreatedEvent.of(savedOrder));

        // 예외 발생이 이벤트 퍼블리쉬 아래에 있다면 기본 리스너는 실행되고
        // 트랜잭션 이벤트 리스너는 실행 안 됨
        if (request.getProductId() == null) {
            throw new IllegalArgumentException();
        }

        return OrderResponse.from(savedOrder);
    }
}

