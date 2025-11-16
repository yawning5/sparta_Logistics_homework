package com.sparta.practiceorder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sparta.practiceorder.dto.OrderCreateRequest;
import com.sparta.practiceorder.events.OrderCreatedEvent;
import com.sparta.practiceorder.listener.PaymentEventListener;
import com.sparta.practiceorder.repository.OrderRepository;
import com.sparta.practiceorder.service.OrderService;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
public class TransactionalEventTest {

    @Autowired
    private OrderService orderService;

    @MockitoSpyBean
    private PaymentEventListener paymentEventListener;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("트랜잭션 롤백 시 이벤트가 발행되지 않는다")
    void whenTransactionRollback_EventShouldNotBePublished() {
        OrderCreateRequest request = createInvalidRequest(); // 예외를 발생시킬 요청

        assertThatThrownBy(() -> orderService.createOrder(request))
            .isInstanceOf(IllegalArgumentException.class);

        assertThat(orderRepository.findAll()).isEmpty();

        verify(paymentEventListener, never())
            .handleOrderCreated(any(OrderCreatedEvent.class));
    }

    private OrderCreateRequest createInvalidRequest() {
        return OrderCreateRequest.builder().build();
    }

    @Test
    @DisplayName("트랜잭션 커밋 성공 시 이벤트가 발행된다")
    void whenTransactionCommit_EventShouldBePublished() {
        // given
        OrderCreateRequest request = createValidRequest();

        // when
        orderService.createOrder(request);

        // then
        assertThat(orderRepository.findAll()).hasSize(1);

        verify(paymentEventListener, times(1))
            .handleOrderCreated(any(OrderCreatedEvent.class));
    }

    private OrderCreateRequest createValidRequest() {
        return OrderCreateRequest.builder()
            .quantity(1)
            .supplierId(UUID.randomUUID())
            .receiverId(UUID.randomUUID())
            .productId(UUID.randomUUID())
            .build();
    }
}
