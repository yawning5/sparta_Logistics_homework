package com.keepgoing.order.application.service.order;

import static org.assertj.core.api.Assertions.assertThat;

import com.keepgoing.order.application.dto.CreateOrderCommand;
import com.keepgoing.order.domain.order.Order;
import com.keepgoing.order.domain.order.OrderState;
import com.keepgoing.order.domain.payment.Payment;
import com.keepgoing.order.domain.payment.PaymentStatus;
import com.keepgoing.order.domain.product.Product;
import com.keepgoing.order.infrastructure.order.OrderRepository;
import com.keepgoing.order.infrastructure.payment.PaymentRepository;
import com.keepgoing.order.infrastructure.product.ProductRepository;
import com.keepgoing.order.presentation.dto.response.api.CreateOrderResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
class OrderFlowIntegrationTest {

    @Autowired
    private OrderEventPracticeService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    @DisplayName("주문 생성부터 재고 차감까지 전체 플로우가 정상 동작한다")
    void fullOrderFlow_ShouldWorkCorrectly() throws InterruptedException {
        // given
        Product product = Product.builder()
            .id(UUID.randomUUID())
            .name("테스트 상품")
            .stock(100)
            .build();
        productRepository.save(product);

        CreateOrderCommand request = CreateOrderCommand.builder()
            .memberId(1L)
            .supplierId(UUID.randomUUID())
            .supplierName("오징어 집합체")
            .receiverId(UUID.randomUUID())
            .receiverName("오징어 모음체")
            .productId(product.getId())
            .productName("오징어")
            .quantity(10)
            .price(10000000)
            .deliveryDueAt(LocalDateTime.now().plusDays(1))
            .deliveryRequestNote("오래걸려용")
            .build();

        // when
        CreateOrderResponse response = orderService.createOrder(request);

        // 비동기 처리를 위한 대기
        Thread.sleep(3000);

        // then
        // 1. 주문 생성 확인
        Order order = orderRepository.findById(UUID.fromString(response.getOrderId()))
            .orElseThrow();
        assertThat(order.getOrderState()).isEqualTo(OrderState.PENDING_VALIDATION);

        // 2. 결제 생성 확인
        List<Payment> payments = paymentRepository.findByOrderId(order.getId());
        assertThat(payments).hasSize(1);
        assertThat(payments.get(0).getStatus()).isEqualTo(PaymentStatus.COMPLETED);

        // 3. 재고 차감 확인
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStock()).isEqualTo(90);

        // 이렇게 전체 플로우가 자동으로 연결되어 실행됩니다.
        // 각 서비스는 서로를 직접 호출하지 않지만, 이벤트를 통해 협력합니다.
    }

    @Test
    @DisplayName("결제 실패 시 재고는 차감되지 않는다")
    void whenPaymentFails_StockShouldNotDecrease() throws InterruptedException {
        // given
        Product product = Product.builder()
            .id(UUID.randomUUID())
            .stock(100)
            .build();
        productRepository.save(product);

        // 결제 실패 시뮬레이션을 위한 Mock 설정
        // 실무에서는 PG사 연동 실패, 잔액 부족 등의 시나리오를 테스트합니다.

        // when
        CreateOrderCommand request = CreateOrderCommand.builder()
            .memberId(1L)
            .supplierId(UUID.randomUUID())
            .supplierName("오징어 집합체")
            .receiverId(UUID.randomUUID())
            .receiverName("오징어 모음체")
            .productId(product.getId())
            .productName("오징어")
            .quantity(50) // quantity가 50일때만 결제에서 재고차감 이벤트 발행 X
            .price(10000000)
            .deliveryDueAt(LocalDateTime.now().plusDays(1))
            .deliveryRequestNote("오래걸려용")
            .build();

        orderService.createOrder(request);
        Thread.sleep(2000);

        // then: 결제가 실패했으므로 재고는 그대로 유지됩니다.
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStock()).isEqualTo(100);

        // 이처럼 이벤트 체인에서 중간 단계가 실패하면,
        // 다음 단계가 실행되지 않아 데이터 일관성이 유지됩니다.
    }
}
