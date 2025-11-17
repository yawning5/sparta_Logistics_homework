package com.sparta.practiceorder;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.sparta.practiceorder.order.dto.OrderCreateRequest;
import com.sparta.practiceorder.order.event.OrderCreatedEvent;
import com.sparta.practiceorder.payment.event.PaymentEventListener;
import com.sparta.practiceorder.order.repository.OrderRepository;
import com.sparta.practiceorder.order.service.OrderService;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest
public class TransactionalEventTest {

    @Autowired
    private OrderService orderService;

    @MockitoSpyBean
    private PaymentEventListener paymentEventListener;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        orderRepository.deleteAll();
    }

    @AfterEach
    void afterEach() {
        orderRepository.deleteAll();
    }

    @Test
    @DisplayName("트랜잭션 롤백 시 이벤트가 발행되지 않는다")
    void whenTransactionRollback_EventShouldNotBePublished() {
        OrderCreateRequest request = createInvalidRequest(); // 예외를 발생시킬 요청

        // when & then
        // 주문 생성 중 예외가 발생하여 트랜잭션이 롤백됩니다.
        assertThatThrownBy(() -> orderService.createOrder(request))
            .isInstanceOf(IllegalArgumentException.class);

        // 트랜잭션이 롤백되었으므로, 주문도 저장되지 않고
        assertThat(orderRepository.findAll()).isEmpty();

        // 이벤트 리스너도 실행되지 않습니다.
        verify(paymentEventListener, never())
            .handleOrderCreated(any(OrderCreatedEvent.class));

        // 이를 통해 데이터 일관성이 유지됩니다.
        // "주문은 없는데 결제는 진행된다"는 상황을 방지할 수 있습니다.
    }

    private OrderCreateRequest createInvalidRequest() {
        return OrderCreateRequest.builder().build();
    }

    @Test
    @DisplayName("트랜잭션 커밋 성공 시 이벤트가 발행된다")
    @Rollback(false)
    void whenTransactionCommit_EventShouldBePublished() {
        // given
        OrderCreateRequest request = createValidRequest();

        // when
        orderService.createOrder(request);

        // then
        // 트랜잭션이 성공적으로 커밋되었으므로
        assertThat(orderRepository.findAll()).hasSize(1);

        // 이벤트 리스너도 실행됩니다.
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
