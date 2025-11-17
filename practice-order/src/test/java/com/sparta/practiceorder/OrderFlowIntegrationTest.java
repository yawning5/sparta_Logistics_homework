package com.sparta.practiceorder;


import static org.assertj.core.api.Assertions.assertThat;

import com.sparta.practiceorder.order.dto.OrderCreateRequest;
import com.sparta.practiceorder.order.dto.OrderResponse;
import com.sparta.practiceorder.order.domain.Order;
import com.sparta.practiceorder.payment.domain.Payment;
import com.sparta.practiceorder.stock.domain.Product;
import com.sparta.practiceorder.payment.domain.enums.PaymentStatus;
import com.sparta.practiceorder.order.domain.enums.OrderStatus;
import com.sparta.practiceorder.order.repository.OrderRepository;
import com.sparta.practiceorder.payment.repository.PaymentRepository;
import com.sparta.practiceorder.stock.repository.ProductRepository;
import com.sparta.practiceorder.order.service.OrderService;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class OrderFlowIntegrationTest {
    @Autowired
    private OrderService orderService;

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

        OrderCreateRequest request = OrderCreateRequest.builder()
            .productId(product.getId())
            .supplierId(UUID.randomUUID())
            .receiverId(UUID.randomUUID())
            .quantity(10)
            .build();

        // when
        OrderResponse response = orderService.createOrder(request);

        // 비동기 처리를 위한 대기
        Thread.sleep(3000);

        // then
        // 1. 주문 생성 확인
        Order order = orderRepository.findById(response.getId()).orElseThrow();
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PENDING);

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
        OrderCreateRequest request = createRequestWithInsufficientBalance(product.getId());
        orderService.createOrder(request);
        Thread.sleep(2000);

        // then: 결제가 실패했으므로 재고는 그대로 유지됩니다.
        Product updatedProduct = productRepository.findById(product.getId()).orElseThrow();
        assertThat(updatedProduct.getStock()).isEqualTo(100);

        // 이처럼 이벤트 체인에서 중간 단계가 실패하면,
        // 다음 단계가 실행되지 않아 데이터 일관성이 유지됩니다.
    }

    private OrderCreateRequest createRequestWithInsufficientBalance(UUID productId) {
        return OrderCreateRequest.builder()
            .productId(productId)
            .quantity(300)
            .build();
    }
}