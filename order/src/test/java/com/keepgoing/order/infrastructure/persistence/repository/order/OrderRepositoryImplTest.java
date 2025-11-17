package com.keepgoing.order.infrastructure.persistence.repository.order;

import static org.junit.jupiter.api.Assertions.*;

import com.keepgoing.order.application.repository.OrderRepository;
import com.keepgoing.order.domain.model.Order;
import com.keepgoing.order.domain.state.CancelState;
import com.keepgoing.order.domain.state.OrderState;
import com.keepgoing.order.infrastructure.persistence.mapper.OrderMapper;
import com.keepgoing.order.presentation.config.external.JpaConfig;
import jakarta.persistence.EntityManager;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest(
    properties = {
        "spring.cloud.config.enabled=false",
        "spring.cloud.config.import=",
        "eureka.client.enabled=false",
        "eureka.client.register-with-eureka=false",
        "eureka.client.fetch-registry=false",
    }
)
@Import({
        JpaConfig.class,
        OrderRepositoryImpl.class,
        OrderMapper.class
})
@ActiveProfiles("test")
class OrderRepositoryImplTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    EntityManager entityManager;


    @DisplayName("save 메서드를 실행할 때 값들이 모두 올바르게 들어오면 DB에 문제 없이 저장된다.")
    @Test
    void orderRepository_save_test() {
        // given
        Clock fixedClock = Clock.fixed(
            Instant.parse("2025-11-15T00:00:00Z"),
            ZoneId.of("UTC")
        );


        LocalDateTime now = LocalDateTime.now(fixedClock);

        Order order = Order.create(
            1L,
            UUID.fromString("5cfb8c2c-4c40-48a4-9e87-1f6b0d282f32"),
            UUID.fromString("8c1c3b63-7dfb-4c2f-987d-b5421fa8ef7e"),
            UUID.fromString("d5bb2f27-e9f9-4d22-af91-0f1a0b8c9ad5"),
            LocalDateTime.of(2025, 11, 15, 20, 38, 0),
            "정각에 배달 부탁드립니다.",
            1000,
            50000,
            UUID.fromString("e72d5379-c97d-4777-8437-0bd966819c56"),
            now
        );

        // when
        order = orderRepository.save(order);

        entityManager.flush();
        entityManager.clear();

        Order result = orderRepository.findById(order.getId());

        // then
        Assertions.assertThat(result.getId()).isNotNull();
        Assertions.assertThat(result.getMember().memberId()).isEqualTo(1L);
        Assertions.assertThat(result.getSupplier().vendorId())
            .isEqualTo(UUID.fromString("5cfb8c2c-4c40-48a4-9e87-1f6b0d282f32"));
        Assertions.assertThat(result.getReceiver().vendorId())
            .isEqualTo(UUID.fromString("8c1c3b63-7dfb-4c2f-987d-b5421fa8ef7e"));
        Assertions.assertThat(result.getProduct().productId())
            .isEqualTo(UUID.fromString("d5bb2f27-e9f9-4d22-af91-0f1a0b8c9ad5"));
        Assertions.assertThat(result.getTotalPrice()).isEqualTo(50000000);
        Assertions.assertThat(result.getOrderState()).isEqualTo(OrderState.PENDING);
        Assertions.assertThat(result.getCancelState()).isEqualTo(CancelState.NONE);
    }
}