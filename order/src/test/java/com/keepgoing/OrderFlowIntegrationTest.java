package com.keepgoing;

import static org.junit.jupiter.api.Assertions.*;

import com.keepgoing.order.application.dto.CreateOrderCommand;
import com.keepgoing.order.application.repository.OrderRepository;
import com.keepgoing.order.application.service.OrderService;
import com.keepgoing.order.domain.model.Order;
import com.keepgoing.order.domain.state.CancelState;
import com.keepgoing.order.domain.state.OrderState;
import com.keepgoing.order.presentation.dto.response.api.CreateOrderResponse;
import com.keepgoing.payment.application.repo.PaymentRepository;
import com.keepgoing.payment.domain.Payment;
import com.keepgoing.payment.domain.PaymentStatus;
import com.keepgoing.product.application.repository.ProductRepository;
import com.keepgoing.product.domain.Product;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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
class OrderFlowIntegrationTest {

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("주문 생성부터 재고 차감까지 전체 플로우가 정상 동작한다.")
    @Test
    void fullOrderFlowShouldWorkCorrectly() throws InterruptedException {
        // given
        Product product = Product.builder()
            .id(UUID.randomUUID())
            .name("무한동력기")
            .price(100000)
            .stock(100)
            .build();
        productRepository.save(product);

        CreateOrderCommand command = CreateOrderCommand.builder()
            .memberId(1L)
            .supplierId(UUID.randomUUID())
            .supplierName("공급업체A")
            .receiverId(UUID.randomUUID())
            .receiverName("수령자B")
            .productId(product.getId())
            .productName("상품C")
            .quantity(10)
            .price(1000000)
            .deliveryDueAt(LocalDateTime.now().plusDays(1))
            .deliveryRequestNote("문 앞에 놔주세요")
            .build();

        // when
        CreateOrderResponse response = orderService.createOrder(command);

        Thread.sleep(500);
        UUID orderId = UUID.fromString(response.orderId());

        // then
        // 주문이 정상적으로 생성되었는지 확인
        Assertions.assertThat(orderId).isNotNull();
        Order order = orderRepository.findById(orderId);
        Assertions.assertThat(order.getCancelState()).isEqualTo(CancelState.NONE);

        // 결제가 정상적으로 처리되었는지 확인
        List<Payment> payments = paymentRepository.findByOrderId(orderId);
        Assertions.assertThat(payments).hasSize(1);
        Assertions.assertThat(payments.get(0).getStatus()).isEqualTo(PaymentStatus.COMPLETED);

        // 상품의 재고가 정상적으로 차감되었는지 확인
        Product updatedProduct = productRepository.findById(product.getId());
        Assertions.assertThat(updatedProduct.getStock()).isEqualTo(90);

        // 재고 차감 이후, 주문이 완료되었음 상태로 잘 변경되었는지 확인
        Assertions.assertThat(order.getOrderState()).isEqualTo(OrderState.COMPLETED);
    }
}