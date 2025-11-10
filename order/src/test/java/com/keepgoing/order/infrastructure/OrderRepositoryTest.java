package com.keepgoing.order.infrastructure;

import com.keepgoing.order.config.external.JpaConfig;
import com.keepgoing.order.domain.order.Order;
import com.keepgoing.order.domain.order.OrderState;
import com.keepgoing.order.infrastructure.order.OrderRepository;
import java.time.LocalDateTime;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(properties = "spring.cloud.config.enabled=false")
@Import(JpaConfig.class)
@Transactional
class OrderRepositoryTest {

    @Autowired
    OrderRepository orderRepository;

    @DisplayName("주문이 정상적으로 저장됩니다.")
    @Test
    void saveOrderEntityTest() {
        // given
        LocalDateTime now = LocalDateTime.of(2025, 11, 5, 12, 0, 0);

        // when
        Order order = Order.create(
            1L,
            UUID.fromString("4e88649d-37ae-4b0f-b2a2-8b79e6912a9c"),
            "김이 맛있는 집",
            UUID.fromString("7027b9af-2c0c-4f04-b04d-6d2e27bd1928"),
            "김이 필요한 집",
            UUID.fromString("b8e2e021-2e59-4c8f-9c17-a138e8e61c9c"),
            "맛있는 김",
            1,
            100000,
            now,
            LocalDateTime.of(2025, 11, 6, 12, 0, 0),
            "맛있는 김으로 배송해주세요."
        );

        // when
        Order saveOrder = orderRepository.save(order);

        // then
        Assertions.assertThat(saveOrder.getSupplierId())
            .isEqualTo(UUID.fromString("4e88649d-37ae-4b0f-b2a2-8b79e6912a9c"));
        Assertions.assertThat(saveOrder.getSupplierName())
            .isEqualTo("김이 맛있는 집");

        Assertions.assertThat(saveOrder.getReceiverId())
            .isEqualTo(UUID.fromString("7027b9af-2c0c-4f04-b04d-6d2e27bd1928"));
        Assertions.assertThat(saveOrder.getReceiverName())
            .isEqualTo("김이 필요한 집");

        Assertions.assertThat(saveOrder.getProductId())
            .isEqualTo(UUID.fromString("b8e2e021-2e59-4c8f-9c17-a138e8e61c9c"));
        Assertions.assertThat(saveOrder.getProductName())
            .isEqualTo("맛있는 김");
        Assertions.assertThat(saveOrder.getOrderState()).isEqualTo(OrderState.PENDING_VALIDATION);
        Assertions.assertThat(saveOrder.getOrderedAt()).isEqualTo(now);
        Assertions.assertThat(saveOrder.getDeliveryDueAt()).isEqualTo(
            LocalDateTime.of(2025, 11, 6, 12, 0, 0)
        );
        Assertions.assertThat(saveOrder.getDeliveryRequestNote()).isEqualTo(
            "맛있는 김으로 배송해주세요."
        );
        Assertions.assertThat(saveOrder.getCancelledAt()).isNull();
        Assertions.assertThat(saveOrder.getConfirmedAt()).isNull();
        Assertions.assertThat(saveOrder.getDeletedAt()).isNull();
        Assertions.assertThat(saveOrder.getDeletedBy()).isNull();
    }
}