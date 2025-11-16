package com.keepgoing.order.application.service;

import com.keepgoing.order.application.dto.CreateOrderCommand;
import com.keepgoing.order.application.repository.OrderRepository;
import com.keepgoing.order.domain.event.OrderCreatedEvent;
import com.keepgoing.order.domain.model.Order;
import com.keepgoing.order.presentation.dto.response.api.CreateOrderResponse;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final Clock clock;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand command) {

        Order order = Order.create(
            command.memberId(),
            command.supplierId(),
            command.receiverId(),
            command.productId(),
            command.deliveryDueAt(),
            command.deliveryRequestNote(),
            command.quantity(),
            command.price(),
            UUID.randomUUID(),
            LocalDateTime.now(clock)
        );

        order = orderRepository.save(order);

        eventPublisher.publishEvent(OrderCreatedEvent.of(order, LocalDateTime.now(clock)));

        orderRepository.throwException();

        return CreateOrderResponse.from(order);
    }
}
