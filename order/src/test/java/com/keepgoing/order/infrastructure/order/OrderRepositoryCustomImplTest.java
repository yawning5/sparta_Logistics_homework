package com.keepgoing.order.infrastructure.order;

import static org.junit.jupiter.api.Assertions.*;

import com.keepgoing.order.config.external.JpaConfig;
import com.keepgoing.order.domain.order.Order;
import com.keepgoing.order.domain.order.OrderState;
import jakarta.persistence.EntityManager;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
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
            .memberId(1L)
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
            .memberId(1L)
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
            .memberId(1L)
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
            .memberId(1L)
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

    @DisplayName("특정 상태를 갖는 주문들의 ID 목록을 가져오는 findPendingIds() 테스트")
    @Test
    void findPendingIdsTest() {

        // given
        Order order1 = Order.builder()
            .memberId(1L)
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

        ReflectionTestUtils.setField(order1, "createdAt", LocalDateTime.of(2025, 10, 10, 13, 3, 0));

        Order order2 = Order.builder()
            .memberId(1L)
            .supplierId(UUID.randomUUID())
            .supplierName("B공급업체")
            .receiverId(UUID.randomUUID())
            .receiverName("B수령업체")
            .productId(UUID.randomUUID())
            .productName("상품B")
            .deliveryId(UUID.randomUUID())
            .orderState(OrderState.PRODUCT_VERIFIED)
            .quantity(2)
            .totalPrice(5000)
            .idempotencyKey(UUID.randomUUID())
            .deliveryDueAt(LocalDateTime.now().plusDays(3))
            .deliveryRequestNote("부재 시 문 앞")
            .orderedAt(LocalDateTime.now().minusDays(1))
            .build();

        ReflectionTestUtils.setField(order2, "createdAt", LocalDateTime.of(2025, 10, 10, 12, 3, 0));

        Order order3 = Order.builder()
            .memberId(1L)
            .supplierId(UUID.randomUUID())
            .supplierName("C공급업체")
            .receiverId(UUID.randomUUID())
            .receiverName("C수령업체")
            .productId(UUID.randomUUID())
            .productName("상품C")
            .deliveryId(UUID.randomUUID())
            .orderState(OrderState.COMPLETION_IN_PROGRESS)
            .quantity(3)
            .totalPrice(3000)      // 가격은 낮지만
            .idempotencyKey(UUID.randomUUID())
            .deliveryDueAt(LocalDateTime.now().plusDays(3))
            .deliveryRequestNote("배송 전 연락")
            .orderedAt(LocalDateTime.now()) // 가장 최신
            .build();

        ReflectionTestUtils.setField(order3, "createdAt", LocalDateTime.of(2025, 10, 10, 11, 3, 0));

        Order order4 = Order.builder()
            .memberId(1L)
            .supplierId(UUID.randomUUID())
            .supplierName("D공급업체")
            .receiverId(UUID.randomUUID())
            .receiverName("D수령업체")
            .productId(UUID.randomUUID())
            .productName("상품D")
            .deliveryId(UUID.randomUUID())
            .orderState(OrderState.ORDER_CONFIRMED)
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

        Pageable pageable = PageRequest.of(0, 10);

        Set<OrderState> PROCESSING_STATES = Set.of(
            OrderState.PENDING_VALIDATION,
            OrderState.PRODUCT_VERIFIED,
            OrderState.PAID
        );

        // when
        Page<UUID> ids = orderRepository.findPendingIds(PROCESSING_STATES, pageable);

        // then
        Assertions.assertThat(ids.getContent().size()).isEqualTo(2);
    }

    @DisplayName("특정 주문이 상태 변경을 통해 선점을 수행하는지 테스트 ")
    @Test
    void claimTest() {
        // given
        Order order1 = Order.builder()
            .memberId(1L)
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

        entityManager.persist(order1);

        UUID orderId =  orderRepository.findPendingIds(Set.of(OrderState.PENDING_VALIDATION),
                PageRequest.of(0, 10))
            .getContent().getFirst();

        Order order = orderRepository.findById(orderId).orElse(null);

        LocalDateTime now = LocalDateTime.of(2025, 11, 10, 13, 0, 0);
        // when
        int result = orderRepository.claim(
            orderId,
            OrderState.PENDING_VALIDATION,
            OrderState.PRODUCT_VALIDATION_IN_PROGRESS,
            now
        );

        // then
        entityManager.clear();
        Order refreshOrder = orderRepository.findById(orderId).orElse(null);

        Assertions.assertThat(result).isEqualTo(1);
        Assertions.assertThat(refreshOrder.getId()).isEqualTo(orderId);
        Assertions.assertThat(refreshOrder.getOrderState())
            .isEqualTo(OrderState.PRODUCT_VALIDATION_IN_PROGRESS);
    }

    @DisplayName("허브 ID를 삽입하고, 주어진 상태로 잘 전이되는지 확인")
    @Test
    void updateOrderStateToProductVerifiedWithHubTest() {
        // given
        Order order1 = Order.builder()
            .memberId(1L)
            .supplierId(UUID.randomUUID())
            .supplierName("A공급업체")
            .receiverId(UUID.randomUUID())
            .receiverName("A수령업체")
            .productId(UUID.randomUUID())
            .productName("상품A")
            .deliveryId(UUID.randomUUID())
            .orderState(OrderState.PRODUCT_VALIDATION_IN_PROGRESS)
            .quantity(1)
            .totalPrice(3000)      // 가장 낮은 가격
            .idempotencyKey(UUID.randomUUID())
            .deliveryDueAt(LocalDateTime.now().plusDays(3))
            .deliveryRequestNote("요청 없음")
            .orderedAt(LocalDateTime.now().minusDays(3))
            .build();

        entityManager.persist(order1);

        UUID orderId =  orderRepository.findPendingIds(Set.of(OrderState.PRODUCT_VALIDATION_IN_PROGRESS),
                PageRequest.of(0, 10))
            .getContent().getFirst();

        Order order = orderRepository.findById(orderId).orElse(null);

        LocalDateTime now = LocalDateTime.of(2025, 11, 10, 13, 0, 0);

        UUID hubId = UUID.randomUUID();

        // when
        int result = orderRepository.updateOrderStateToProductVerifiedWithHub(orderId, hubId, now);

        // then
        entityManager.clear();
        Order refreshOrder = orderRepository.findById(orderId).orElse(null);

        Assertions.assertThat(result).isEqualTo(1);
        Assertions.assertThat(refreshOrder.getHubId()).isEqualTo(hubId);
        Assertions.assertThat(refreshOrder.getOrderState())
            .isEqualTo(OrderState.PRODUCT_VERIFIED);
    }

    @DisplayName("주문 상태가 AwaitingPayemnt로 잘 전이되는지 확인")
    @Test
    void updateOrderStateToAwaitingPaymentTest() {
        // given
        Order order1 = Order.builder()
            .memberId(1L)
            .supplierId(UUID.randomUUID())
            .supplierName("A공급업체")
            .receiverId(UUID.randomUUID())
            .receiverName("A수령업체")
            .productId(UUID.randomUUID())
            .productName("상품A")
            .deliveryId(UUID.randomUUID())
            .orderState(OrderState.STOCK_RESERVATION_IN_PROGRESS)
            .quantity(1)
            .totalPrice(3000)      // 가장 낮은 가격
            .idempotencyKey(UUID.randomUUID())
            .deliveryDueAt(LocalDateTime.now().plusDays(3))
            .deliveryRequestNote("요청 없음")
            .orderedAt(LocalDateTime.now().minusDays(3))
            .build();

        entityManager.persist(order1);

        UUID orderId =  orderRepository.findPendingIds(Set.of(OrderState.STOCK_RESERVATION_IN_PROGRESS),
                PageRequest.of(0, 10))
            .getContent().getFirst();

        Order order = orderRepository.findById(orderId).orElse(null);

        LocalDateTime now = LocalDateTime.of(2025, 11, 10, 13, 0, 0);

        // when
        int result = orderRepository.updateOrderStateToAwaitingPayment(orderId, now);

        // then
        entityManager.clear();
        Order refreshOrder = orderRepository.findById(orderId).orElse(null);

        Assertions.assertThat(result).isEqualTo(1);
        Assertions.assertThat(refreshOrder.getOrderState())
            .isEqualTo(OrderState.AWAITING_PAYMENT);
    }

    @DisplayName("주문 상태가 Paid로 잘 전이되는지 확인")
    @Test
    void updateOrderStateToPaidTest() {
        // given
        Order order1 = Order.builder()
            .memberId(1L)
            .supplierId(UUID.randomUUID())
            .supplierName("A공급업체")
            .receiverId(UUID.randomUUID())
            .receiverName("A수령업체")
            .productId(UUID.randomUUID())
            .productName("상품A")
            .deliveryId(UUID.randomUUID())
            .orderState(OrderState.AWAITING_PAYMENT)
            .quantity(1)
            .totalPrice(3000)      // 가장 낮은 가격
            .idempotencyKey(UUID.randomUUID())
            .deliveryDueAt(LocalDateTime.now().plusDays(3))
            .deliveryRequestNote("요청 없음")
            .orderedAt(LocalDateTime.now().minusDays(3))
            .build();

        entityManager.persist(order1);

        UUID orderId =  orderRepository.findPendingIds(Set.of(OrderState.AWAITING_PAYMENT),
                PageRequest.of(0, 10))
            .getContent().getFirst();

        Order order = orderRepository.findById(orderId).orElse(null);

        LocalDateTime now = LocalDateTime.of(2025, 11, 10, 13, 0, 0);

        // when
        int result = orderRepository.updateOrderStateToPaidForPayment(
            orderId,
            order.getVersion(),
            now
        );

        // then
        entityManager.clear();
        Order refreshOrder = orderRepository.findById(orderId).orElse(null);

        Assertions.assertThat(result).isEqualTo(1);
        Assertions.assertThat(refreshOrder.getOrderState())
            .isEqualTo(OrderState.PAID);
    }

    @DisplayName("주문 상태가 Completed로 잘 전이되는지 확인")
    @Test
    void updateOrderStateToCompleted() {
        // given
        Order order1 = Order.builder()
            .memberId(1L)
            .supplierId(UUID.randomUUID())
            .supplierName("A공급업체")
            .receiverId(UUID.randomUUID())
            .receiverName("A수령업체")
            .productId(UUID.randomUUID())
            .productName("상품A")
            .deliveryId(UUID.randomUUID())
            .orderState(OrderState.COMPLETION_IN_PROGRESS)
            .quantity(1)
            .totalPrice(3000)      // 가장 낮은 가격
            .idempotencyKey(UUID.randomUUID())
            .deliveryDueAt(LocalDateTime.now().plusDays(3))
            .deliveryRequestNote("요청 없음")
            .orderedAt(LocalDateTime.now().minusDays(3))
            .build();

        entityManager.persist(order1);

        UUID orderId =  orderRepository.findPendingIds(Set.of(OrderState.COMPLETION_IN_PROGRESS),
                PageRequest.of(0, 10))
            .getContent().getFirst();

        Order order = orderRepository.findById(orderId).orElse(null);

        LocalDateTime now = LocalDateTime.of(2025, 11, 10, 13, 0, 0);

        // when
        int result = orderRepository.updateOrderStateToCompleted(orderId, now);

        // then
        entityManager.clear();
        Order refreshOrder = orderRepository.findById(orderId).orElse(null);

        Assertions.assertThat(result).isEqualTo(1);
        Assertions.assertThat(refreshOrder.getOrderState())
            .isEqualTo(OrderState.COMPLETED);
    }

    @DisplayName("주문이 정상적으로 soft Deleted되었습니다.")
    @Test
    void deleteOrder() {
        // given
        Order order1 = Order.builder()
            .memberId(1L)
            .supplierId(UUID.randomUUID())
            .supplierName("A공급업체")
            .receiverId(UUID.randomUUID())
            .receiverName("A수령업체")
            .productId(UUID.randomUUID())
            .productName("상품A")
            .deliveryId(UUID.randomUUID())
            .orderState(OrderState.ORDER_CONFIRMED)
            .quantity(1)
            .totalPrice(3000)      // 가장 낮은 가격
            .idempotencyKey(UUID.randomUUID())
            .deliveryDueAt(LocalDateTime.now().plusDays(3))
            .deliveryRequestNote("요청 없음")
            .orderedAt(LocalDateTime.now().minusDays(3))
            .build();

        entityManager.persist(order1);

        UUID orderId =  orderRepository.findPendingIds(Set.of(OrderState.ORDER_CONFIRMED),
                PageRequest.of(0, 10))
            .getContent().getFirst();

        Order order = orderRepository.findById(orderId).orElse(null);

        LocalDateTime now = LocalDateTime.of(2025, 11, 10, 13, 0, 0);

        // when
        int result = orderRepository.deleteOrder(
            orderId,
            1L,
            now,
            order.getVersion()
        );

        // then
        entityManager.clear();
        Order refreshOrder = orderRepository.findById(orderId).orElse(null);

        Assertions.assertThat(result).isEqualTo(1);
        Assertions.assertThat(refreshOrder).isNull();
    }
}