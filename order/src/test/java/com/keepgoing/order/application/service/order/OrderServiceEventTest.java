package com.keepgoing.order.application.service.order;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.keepgoing.order.application.dto.CreateOrderCommand;
import com.keepgoing.order.application.event.OrderEventListener;
import com.keepgoing.order.application.event.PaymentEventListener;
import com.keepgoing.order.domain.order.event.OrderCreatedEvent;
import com.keepgoing.order.infrastructure.order.OrderRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;


@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
    "spring.datasource.driverClassName=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.jpa.hibernate.ddl-auto=create-drop",
    "spring.cloud.config.enabled=false",
    "eureka.client.enabled=false"
})
class OrderServiceEventTest {

    @Autowired
    private OrderEventPracticeService orderService;

    @MockitoBean
    private OrderEventListener orderEventListener;

    @MockitoSpyBean
    private PaymentEventListener paymentEventListener;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @DisplayName("주문 생성 시 OrderCreatedEvent가 발행된다")
    void createOrder_ShouldPublishOrderCreatedEvent() {
        // given
        CreateOrderCommand request = CreateOrderCommand.builder()
            .memberId(1L)
            .supplierId(UUID.randomUUID())
            .supplierName("오징어 집합체")
            .receiverId(UUID.randomUUID())
            .receiverName("오징어 모음체")
            .productId(UUID.randomUUID())
            .productName("오징어")
            .quantity(5)
            .price(10000000)
            .deliveryDueAt(LocalDateTime.now().plusDays(1))
            .deliveryRequestNote("오래걸려용")
            .build();


        // when
        orderService.createOrder(request);

        // then
        // 이벤트 리스너가 호출되었는지 검증
        verify(orderEventListener, times(1))
            .handleOrderCreated(any(OrderCreatedEvent.class));
    }

    @Test
    @DisplayName("트랜잭션 롤백 시 이벤트가 발행되지 않는다")
    void whenTransactionRollback_EventShouldNotBePublished() {
        // given
       CreateOrderCommand request = CreateOrderCommand.builder()
            .supplierId(UUID.randomUUID())
            .supplierName("오징어 집합체")
            .receiverId(UUID.randomUUID())
            .receiverName("오징어 모음체")
            .productId(UUID.randomUUID())
            .productName("오징어")
            .quantity(5)
            .price(10000000)
            .deliveryDueAt(LocalDateTime.now().plusDays(1))
            .deliveryRequestNote("오래걸려용")
            .build(); // 예외를 발생시킬 요청

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

    @Test
    @DisplayName("트랜잭션 커밋 성공 시 이벤트가 발행된다")
    void whenTransactionCommit_EventShouldBePublished() {
        // given
        CreateOrderCommand request = CreateOrderCommand.builder()
            .memberId(1L)
            .supplierId(UUID.randomUUID())
            .supplierName("오징어 집합체")
            .receiverId(UUID.randomUUID())
            .receiverName("오징어 모음체")
            .productId(UUID.randomUUID())
            .productName("오징어")
            .quantity(5)
            .price(10000000)
            .deliveryDueAt(LocalDateTime.now().plusDays(1))
            .deliveryRequestNote("오래걸려용")
            .build();

        // when
        orderService.createOrder(request);

        // then
        // 트랜잭션이 성공적으로 커밋되었으므로
        assertThat(orderRepository.findAll()).hasSize(1);

        // 이벤트 리스너도 실행됩니다.
        verify(paymentEventListener, times(1))
            .handleOrderCreated(any(OrderCreatedEvent.class));
    }
}
