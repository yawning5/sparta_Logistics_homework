package com.keepgoing.order.application.service.order;

import com.keepgoing.order.application.dto.CreateOrderCommand;
import com.keepgoing.order.application.event.EventPublisher;
import com.keepgoing.order.domain.order.Order;
import com.keepgoing.order.domain.order.event.OrderCreatedEvent;
import com.keepgoing.order.infrastructure.order.OrderRepository;
import com.keepgoing.order.presentation.dto.response.api.CreateOrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderEventPracticeService {

    private final OrderRepository orderRepository;
    private final EventPublisher eventPublisher;

    @Transactional
    public CreateOrderResponse createOrder(CreateOrderCommand createOrderCommand) {
        // 주문을 생성하는 핵심 비즈니스 로직
        Order order = createOrderCommand.toEntity();
        Order saveOrder = orderRepository.save(order);

        // 이벤트 발행: "주문이 생성되었다"는 사실을 알림
        // 누가 이 이벤트를 듣는지, 어떻게 처리하는지는 전혀 모르고 관심도 없습니다.
        eventPublisher.publish(OrderCreatedEvent.of(saveOrder));

        return CreateOrderResponse.create(saveOrder.getId(), saveOrder.getMemberId(),
            saveOrder.getOrderState(), saveOrder.getOrderedAt());
    }

}
