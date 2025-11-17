package com.sparta.practiceorder.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sparta.practiceorder.order.dto.OrderCreateRequest;
import com.sparta.practiceorder.order.event.OrderCreatedEvent;
import com.sparta.practiceorder.order.event.OrderEventListener;
import com.sparta.practiceorder.order.service.OrderService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest

class OrderServiceTest {
    @Autowired
    OrderService orderService;

    @MockitoBean
    OrderEventListener orderEventListener;

    @Test
    @DisplayName("주문 생성 시 OrderCreatedEvent 발행")
    void createOrder_ShouldPublishOrderCreatedEvent() {
        // given
        OrderCreateRequest request = OrderCreateRequest.builder()
            .productId(UUID.randomUUID())
            .receiverId(UUID.randomUUID())
            .supplierId(UUID.randomUUID())
            .quantity(5)
            .build();

        // when
        orderService.createOrder(request);

        //then
        verify(orderEventListener, times(1))
            .handleOrderCreated(any(OrderCreatedEvent.class));
    }
}