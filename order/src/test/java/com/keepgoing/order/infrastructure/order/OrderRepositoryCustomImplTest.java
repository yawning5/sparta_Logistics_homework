package com.keepgoing.order.infrastructure.order;

import static org.junit.jupiter.api.Assertions.*;

import com.keepgoing.order.config.external.JpaConfig;
import com.keepgoing.order.domain.order.Order;
import com.keepgoing.order.domain.order.OrderState;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(
    properties = {
        "spring.cloud.config.enabled=false",
        "spring.cloud.config.import=",
        "eureka.client.enabled=false",
        "eureka.client.register-with-eureka=false",
        "eureka.client.fetch-registry=false",
    }
)
@Import(JpaConfig.class)
@ActiveProfiles("test")
@Transactional
class OrderRepositoryCustomImplTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    EntityManager entityManager;

    @DisplayName("사용자가 지정한 정렬 기준을 바탕으로 데이터를 정렬해서 가져온다.")
    @Test
    void searchOrderPageSortTest() {
        // given
        Sort sort = Sort.by(
            Sort.Order.desc("createdAt"),
            Sort.Order.desc("totalPrice")
        );

        Order order1 = Order.builder()
            .supplierId(UUID.randomUUID())
            .supplierName("A공급업체")
            .receiverId(UUID.randomUUID())
            .receiverName("A수령업체")
            .productId(UUID.randomUUID())
            .productName("상품A")
            .deliveryId(UUID.randomUUID())
            .orderState(OrderState.PENDING_VALIDATION)
            .quantity(1)
            .totalPrice(3000)      // 가장 낮은 가격
            .idempotencyKey(UUID.randomUUID())
            .deliveryDueAt(LocalDateTime.now().plusDays(3))
            .deliveryRequestNote("요청 없음")
            .orderedAt(LocalDateTime.now().minusDays(3))
            .build();

        ReflectionTestUtils.setField(order1, "createdAt", LocalDateTime.of(2025, 10, 10, 12, 3, 0));

        Order order2 = Order.builder()
            .supplierId(UUID.randomUUID())
            .supplierName("B공급업체")
            .receiverId(UUID.randomUUID())
            .receiverName("B수령업체")
            .productId(UUID.randomUUID())
            .productName("상품B")
            .deliveryId(UUID.randomUUID())
            .orderState(OrderState.PENDING_VALIDATION)
            .quantity(2)
            .totalPrice(5000)
            .idempotencyKey(UUID.randomUUID())
            .deliveryDueAt(LocalDateTime.now().plusDays(3))
            .deliveryRequestNote("부재 시 문 앞")
            .orderedAt(LocalDateTime.now().minusDays(1))
            .build();

        ReflectionTestUtils.setField(order2, "createdAt", LocalDateTime.of(2025, 10, 10, 12, 3, 0));

        Order order3 = Order.builder()
            .supplierId(UUID.randomUUID())
            .supplierName("C공급업체")
            .receiverId(UUID.randomUUID())
            .receiverName("C수령업체")
            .productId(UUID.randomUUID())
            .productName("상품C")
            .deliveryId(UUID.randomUUID())
            .orderState(OrderState.PENDING_VALIDATION)
            .quantity(3)
            .totalPrice(3000)      // 가격은 낮지만
            .idempotencyKey(UUID.randomUUID())
            .deliveryDueAt(LocalDateTime.now().plusDays(3))
            .deliveryRequestNote("배송 전 연락")
            .orderedAt(LocalDateTime.now()) // 가장 최신
            .build();

        ReflectionTestUtils.setField(order3, "createdAt", LocalDateTime.of(2025, 9, 10, 12, 3, 0));

        Order order4 = Order.builder()
            .supplierId(UUID.randomUUID())
            .supplierName("D공급업체")
            .receiverId(UUID.randomUUID())
            .receiverName("D수령업체")
            .productId(UUID.randomUUID())
            .productName("상품D")
            .deliveryId(UUID.randomUUID())
            .orderState(OrderState.PENDING_VALIDATION)
            .quantity(4)
            .totalPrice(10000)     // 가장 높은 가격
            .idempotencyKey(UUID.randomUUID())
            .deliveryDueAt(LocalDateTime.now().plusDays(3))
            .deliveryRequestNote("벨 누르지 말기")
            .orderedAt(LocalDateTime.now().minusHours(1))
            .build();

        ReflectionTestUtils.setField(order4, "createdAt", LocalDateTime.of(2025, 9, 9, 12, 3, 0));

        entityManager.persist(order1);
        entityManager.persist(order2);
        entityManager.persist(order3);
        entityManager.persist(order4);

        entityManager.flush();
        entityManager.clear();

        Pageable pageable = PageRequest.of(0, 10, sort);

        // when
        Page<Order> result = orderRepository.searchOrderPage(pageable);
        List<Order> orderList = result.getContent();
        System.out.println("orderList = " + orderList);

        // then
        Assertions.assertThat(orderList.get(0).getSupplierName()).isEqualTo("B공급업체");
        Assertions.assertThat(orderList.get(1).getSupplierName()).isEqualTo("A공급업체");
        Assertions.assertThat(orderList.get(2).getSupplierName()).isEqualTo("C공급업체");
        Assertions.assertThat(orderList.get(3).getSupplierName()).isEqualTo("D공급업체");
    }

}