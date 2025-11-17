package com.keepgoing.order.event;

import com.keepgoing.order.application.dto.CreateOrderCommand;
import com.keepgoing.order.application.event.order.OrderAsyncCreateEvent;
import com.keepgoing.order.application.event.order.OrderCreatedEvent;
import com.keepgoing.order.application.event.order.OrderEventAfterCommitEvent;
import com.keepgoing.order.application.event.order.OrderEventListener;
import com.keepgoing.order.application.event.payment.Payment;
import com.keepgoing.order.application.event.payment.PaymentCompletedEvent;
import com.keepgoing.order.application.event.payment.PaymentEventListener;
import com.keepgoing.order.application.event.payment.PaymentRepository;
import com.keepgoing.order.application.event.stock.Stock;
import com.keepgoing.order.application.event.stock.StockEventListener;
import com.keepgoing.order.application.event.stock.StockRepository;
import com.keepgoing.order.application.service.order.OrderService;
import com.keepgoing.order.domain.order.Order;
import com.keepgoing.order.domain.order.OrderState;
import com.keepgoing.order.infrastructure.order.OrderRepository;
import com.keepgoing.order.presentation.dto.response.api.CreateOrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.awaitility.Awaitility.await;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@EmbeddedKafka(partitions = 1)
class OrderEventTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private OrderService orderService;

    @SpyBean
    private OrderEventListener orderEventListener;

    @SpyBean
    private OrderEventAfterCommitEvent orderEventAfterCommitEvent;

    @SpyBean
    private OrderAsyncCreateEvent orderAsyncCreateEvent;

    @SpyBean
    private PaymentEventListener paymentEventListener;

    @SpyBean
    private StockEventListener stockEventListener;

    @Test
    @DisplayName("주문 생성 시 OrderCreateEvent 발행")
    void orderCreateEvent() {
        // given
        CreateOrderCommand createOrderCommand = CreateOrderCommand.create(
                123L,
                UUID.randomUUID(),
                "Healing Garden Supplier",
                UUID.randomUUID(),
                "User Receiver",
                UUID.randomUUID(),
                "Digital Garden Seed Kit",
                2,
                15000,
                LocalDateTime.of(2026, 1, 15, 10, 30),
                "문 앞에 놓아주세요"
        );

        // when
        CreateOrderResponse savedOrder = orderService.create(createOrderCommand);

        // then
        ArgumentCaptor<OrderCreatedEvent> captor = ArgumentCaptor.forClass(OrderCreatedEvent.class);
        verify(orderEventListener, times(1)).orderCreatedEvent(captor.capture());
    }

    @Test
    @DisplayName("트랜젝션 커밋 성공 시 이벤트 발행")
    void orderCreateEventAfterCommit() {
        // given
        CreateOrderCommand createOrderCommand = CreateOrderCommand.create(
                123L,
                UUID.randomUUID(),
                "Healing Garden Supplier",
                UUID.randomUUID(),
                "User Receiver",
                UUID.randomUUID(),
                "Digital Garden Seed Kit",
                2,
                15000,
                LocalDateTime.of(2026, 1, 15, 10, 30),
                "문 앞에 놓아주세요"
        );

        // when
        orderService.create(createOrderCommand);

        // then
        verify(orderEventAfterCommitEvent, times(1)).orderCreatedEventAfterCommit(any(OrderCreatedEvent.class));
    }

    @Test
    @DisplayName("트랜젝션 롤백 시 이벤트 발행 실패")
    void orderCreateEventAfterRollback() {
        // given
        CreateOrderCommand createOrderCommand = CreateOrderCommand.create(
                123L,
                UUID.randomUUID(),
                "Healing Garden Supplier",
                UUID.randomUUID(),
                "User Receiver",
                UUID.randomUUID(),
                "Digital Garden Seed Kit",
                0,
                15000,
                LocalDateTime.of(2025, 1, 15, 10, 30),
                "문 앞에 놓아주세요"
        );

        // when
        assertThatThrownBy(() -> orderService.create(createOrderCommand))
                .isInstanceOf(IllegalArgumentException.class);

        // then
        assertThat(orderRepository.findAll()).isEmpty();
        verify(orderEventAfterCommitEvent, never())
                .orderCreatedEventAfterCommit(any(OrderCreatedEvent.class));
    }

    @Test
    @DisplayName("이벤트가 비동기로 실행된다.")
    void orderAsyncEventAfterCommit() throws InterruptedException {
        // given
        int eventCount = 5;

        List<CreateOrderCommand> commands = new ArrayList<>();
        for (int i = 0; i < eventCount; i++) {
            commands.add(CreateOrderCommand.create(
                    123L,
                    UUID.randomUUID(),
                    "Supplier " + i,
                    UUID.randomUUID(),
                    "Receiver " + i,
                    UUID.randomUUID(),
                    "Product " + i,
                    i + 1,
                    10000 + i * 1000,
                    LocalDateTime.of(2026, 1, 15, 10, 30),
                    "Delivery note " + i
            ));
        }

        // when
        for (CreateOrderCommand cmd : commands) {
            orderService.create(cmd);
        }

        // 비동기 이벤트가 실행될 시간 확보
        Thread.sleep(3000);

        // then
        // 이벤트가 여러 번 호출되었는지
        await().atMost(2, TimeUnit.SECONDS)
                .untilAsserted(() ->
                        verify(orderAsyncCreateEvent, times(eventCount))
                                .orderAsyncCreatedEventAfterCommit(any(OrderCreatedEvent.class))
                );

    }

    @Test
    @DisplayName("이벤트 체이닝 성공")
    void success_event() throws InterruptedException {
        // given
        CreateOrderCommand createOrderCommand = CreateOrderCommand.create(
                123L,
                UUID.randomUUID(),
                "Healing Garden Supplier",
                UUID.randomUUID(),
                "User Receiver",
                UUID.randomUUID(),
                "Digital Garden Seed Kit",
                2,
                15000,
                LocalDateTime.of(2026, 1, 15, 10, 30),
                "문 앞에 놓아주세요"
        );

        Stock stock = Stock.create(createOrderCommand.productId());
        stockRepository.save(stock);

        //when
        CreateOrderResponse response = orderService.create(createOrderCommand);

        // then
        // 1. 주문 생성 확인
        Order order = orderRepository.findById(UUID.fromString(response.getOrderId()))
                .orElseThrow();
        assertThat(order.getOrderState()).isEqualTo(OrderState.PENDING_VALIDATION);

        // 2. 결제 생성 확인
        List<Payment> payments = paymentRepository.findByOrderId(order.getId());
        assertThat(payments).hasSize(1);
        assertThat(payments.getFirst().getStatus()).isEqualTo("COMPLETED");

        // 3. 재고 차감 확인
        Stock updatedStock = stockRepository.findById(stock.getId()).orElseThrow();
        assertThat(updatedStock.getId()).isEqualTo(stock.getId());
        assertThat(updatedStock.getStock()).isEqualTo(8);

        // then
        verify(paymentEventListener, times(1)).paymentCreatedEventAfterCommit(any(OrderCreatedEvent.class));
        verify(stockEventListener, times(1)).decreaseStock(any(PaymentCompletedEvent.class));
    }

    @Test
    @DisplayName("이벤트 체이닝 실패 - 재고 차감 x")
    void fail_event() throws InterruptedException {
        // given
        CreateOrderCommand createOrderCommand = CreateOrderCommand.create(
                123L,
                UUID.randomUUID(),
                "Healing Garden Supplier",
                UUID.randomUUID(),
                "User Receiver",
                UUID.randomUUID(),
                "Digital Garden Seed Kit",
                100,
                15000,
                LocalDateTime.of(2026, 1, 15, 10, 30),
                "문 앞에 놓아주세요"
        );

        Stock stock = Stock.create(createOrderCommand.productId());
        stockRepository.save(stock);

        //when
        CreateOrderResponse response = orderService.create(createOrderCommand);

        // then
        // 1. 주문 생성 확인
        Order order = orderRepository.findById(UUID.fromString(response.getOrderId()))
                .orElseThrow();
        assertThat(order.getOrderState()).isEqualTo(OrderState.PENDING_VALIDATION);

        // 2. 결제 생성 확인
        List<Payment> payments = paymentRepository.findByOrderId(order.getId());
        assertThat(payments).hasSize(1);
        assertThat(payments.getFirst().getStatus()).isEqualTo("COMPLETED");

        // 3. 재고 차감 x 확인
        Stock updatedStock = stockRepository.findById(stock.getId()).orElseThrow();
        assertThat(updatedStock.getId()).isEqualTo(stock.getId());
        assertThat(updatedStock.getStock()).isEqualTo(10);

        // then
        verify(paymentEventListener, times(1)).paymentCreatedEventAfterCommit(any(OrderCreatedEvent.class));
        verify(stockEventListener, times(1)).decreaseStock(any(PaymentCompletedEvent.class));
    }
}
