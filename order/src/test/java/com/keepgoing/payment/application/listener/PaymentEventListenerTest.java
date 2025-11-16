package com.keepgoing.payment.application.listener;

import static org.junit.jupiter.api.Assertions.*;

import com.keepgoing.order.application.dto.CreateOrderCommand;
import com.keepgoing.order.application.repository.OrderRepository;
import com.keepgoing.order.application.service.OrderService;
import com.keepgoing.order.domain.event.OrderCreatedEvent;
import java.time.LocalDateTime;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringBootTest(
    properties = {
        "spring.cloud.config.enabled=false",
        "spring.cloud.config.import=",
        "eureka.client.enabled=false",
        "eureka.client.register-with-eureka=false",
        "eureka.client.fetch-registry=false",
    }
)
@ActiveProfiles("test")
class PaymentEventListenerTest {

    @Autowired
    private OrderService orderService;

    @MockitoSpyBean
    private OrderRepository orderRepository;

    @MockitoSpyBean
    private PaymentEventListener paymentEventListener;

    @DisplayName("주문을 생성 한 후 트랜잭션에 성공하면 이벤트를 발행하고 결제 이벤트 리스너에서 결제 처리를 수행한다.")
    @Test
    void creatOrderTest() {
        // given
        CreateOrderCommand command = CreateOrderCommand.builder()
            .memberId(1L)
            .supplierId(UUID.randomUUID())
            .supplierName("공급업체A")
            .receiverId(UUID.randomUUID())
            .receiverName("수령자B")
            .productId(UUID.randomUUID())
            .productName("상품C")
            .quantity(1)
            .price(10000)
            .deliveryDueAt(LocalDateTime.now().plusDays(1))
            .deliveryRequestNote("문 앞에 놔주세요")
            .build();

        // when
        orderService.createOrder(command);

        // then
        Mockito.verify(paymentEventListener, Mockito.times(1))
            .handleOrderCreated(Mockito.any(OrderCreatedEvent.class));
    }

    @DisplayName("주문을 생성 시 문제가 발생하여 트랜잭션이 롤백되면 이벤트 리스너는 동작하지 않는다.")
    @Test
    void creatOrderTest2() {
        // given
        CreateOrderCommand command = CreateOrderCommand.builder()
            .memberId(null)
            .supplierId(null)
            .supplierName(null)
            .receiverId(null)
            .receiverName("수령자B")
            .productId(UUID.randomUUID())
            .productName("상품C")
            .quantity(1)
            .price(10000)
            .deliveryDueAt(LocalDateTime.now().plusDays(1))
            .deliveryRequestNote("문 앞에 놔주세요")
            .build();

        // when
        Assertions.assertThatThrownBy(() -> orderService.createOrder(command))
            .isInstanceOf(IllegalArgumentException.class);

        // then
        Mockito.verify(paymentEventListener, Mockito.times(0))
            .handleOrderCreated(Mockito.any(OrderCreatedEvent.class));
    }

    @DisplayName("주문 생성 시 예외가 발생했을 때, 이벤트 발행 전에 예외가 발생하면 AFTER_ROLLBACK이어도 이벤트 리스너는 동작하지 않는다.")
    @Test
    void creatOrderTest3() {
        // given
        CreateOrderCommand command = CreateOrderCommand.builder()
            .memberId(null)
            .supplierId(UUID.randomUUID())
            .supplierName("공급업체A")
            .receiverId(UUID.randomUUID())
            .receiverName("수령자B")
            .productId(UUID.randomUUID())
            .productName("상품C")
            .quantity(1)
            .price(10000)
            .deliveryDueAt(LocalDateTime.now().plusDays(1))
            .deliveryRequestNote("문 앞에 놔주세요")
            .build();

        // when
        Assertions.assertThatThrownBy(() -> orderService.createOrder(command))
            .isInstanceOf(IllegalArgumentException.class);

        // then
        Mockito.verify(paymentEventListener, Mockito.times(0))
            .handleOrderCreatedFail(Mockito.any(OrderCreatedEvent.class));
    }

    @DisplayName("주문 생성 시 예외가 발생했을 때, 이벤트 발행 후에 예외가 발생하면 AFTER_ROLLBACK이어도 이벤트 리스너는 동작한다.")
    @Test
    void creatOrderTest4() {
        // given
        CreateOrderCommand command = CreateOrderCommand.builder()
            .memberId(1L)
            .supplierId(UUID.randomUUID())
            .supplierName("공급업체A")
            .receiverId(UUID.randomUUID())
            .receiverName("수령자B")
            .productId(UUID.randomUUID())
            .productName("상품C")
            .quantity(1)
            .price(10000)
            .deliveryDueAt(LocalDateTime.now().plusDays(1))
            .deliveryRequestNote("문 앞에 놔주세요")
            .build();

        Mockito.doThrow(new RuntimeException("예외 발생시킨다."))
            .when(orderRepository)
                .throwException();

        // when
        Assertions.assertThatThrownBy(() -> orderService.createOrder(command))
            .isInstanceOf(RuntimeException.class);

        // then
        Mockito.verify(paymentEventListener, Mockito.times(1))
            .handleOrderCreatedFail(Mockito.any(OrderCreatedEvent.class));
    }

}