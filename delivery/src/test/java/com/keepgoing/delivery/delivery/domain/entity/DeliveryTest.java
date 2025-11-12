package com.keepgoing.delivery.delivery.domain.entity;

import com.keepgoing.delivery.delivery.domain.vo.Address;
import com.keepgoing.delivery.delivery.domain.vo.Distance;
import com.keepgoing.delivery.delivery.domain.vo.Duration;
import com.keepgoing.delivery.delivery.domain.vo.RouteSeq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class DeliveryTest {

    private Delivery createTestDelivery() {
        return Delivery.create(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Address("서울시 강남구", "테헤란로", "06234"),
                1L,
                "@user123"
        );
    }

    @Test
    @DisplayName("배송 생성 - 성공")
    void createDelivery_Success() {
        // Given
        UUID orderId = UUID.randomUUID();
        UUID departureHubId = UUID.randomUUID();
        UUID destinationHubId = UUID.randomUUID();
        Address address = new Address("서울시 강남구", "테헤란로", "06234");
        Long recipientUserId = 1L;
        String recipientSlackId = "@user123";

        // When
        Delivery delivery = Delivery.create(
                orderId, departureHubId, destinationHubId, address, recipientUserId, recipientSlackId
        );

        // Then
        assertThat(delivery.getId()).isNotNull();
        assertThat(delivery.getOrderId()).isEqualTo(orderId);
        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.HUB_WAITING);
        assertThat(delivery.getDepartureHubId()).isEqualTo(departureHubId);
        assertThat(delivery.getDestinationHubId()).isEqualTo(destinationHubId);
        assertThat(delivery.getAddress()).isEqualTo(address);
    }

    @Test
    @DisplayName("배송 생성 시 필수값 누락 - 실패")
    void createDelivery_WithoutRequiredFields_Fail() {
        // When & Then
        assertThatThrownBy(() ->
                Delivery.create(null, UUID.randomUUID(), UUID.randomUUID(),
                        new Address("서울", "강남", "12345"), 1L, "@user")
        )
                .isInstanceOf(RuntimeException.class)
                .hasMessage("orderId는 필수입니다.");
    }

    @Test
    @DisplayName("배송 경로 추가 - 성공")
    void addRoute_Success() {
        // Given
        Delivery delivery = createTestDelivery();
        DeliveryRoute route = DeliveryRoute.create(
                delivery.getId(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Distance(100.0),
                new Duration(120),
                new RouteSeq(1)
        );

        // When
        delivery.addRoute(route);

        // Then
        assertThat(delivery.getRoutes()).hasSize(1);
        assertThat(delivery.getRoutes().get(0)).isEqualTo(route);
    }

    @Test
    @DisplayName("허브 출발 - 성공")
    void startFromHub_Success() {
        // Given
        Delivery delivery = createTestDelivery();

        // When
        delivery.startFromHub();

        // Then
        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.HUB_IN_PROGRESS);
    }

    @Test
    @DisplayName("HUB_WAITING 상태가 아닐 때 출발 불가 - 실패")
    void startFromHub_NotHubWaiting_Fail() {
        // Given
        Delivery delivery = createTestDelivery();
        delivery.startFromHub();

        // When & Then
        assertThatThrownBy(delivery::startFromHub)
                .isInstanceOf(RuntimeException.class)
                .hasMessage("HUB_WAITING 상태에서만 출발할 수 있습니다.");
    }

    @Test
    @DisplayName("목적지 허브 도착 - 성공")
    void arriveAtDestHub_Success() {
        // Given
        Delivery delivery = createTestDelivery();
        delivery.startFromHub();

        // When
        delivery.arriveAtDestHub();

        // Then
        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.DEST_HUB_ARRIVED);
    }

    @Test
    @DisplayName("업체 배송 시작 - 성공")
    void startVendorDelivery_Success() {
        // Given
        Delivery delivery = createTestDelivery();
        delivery.startFromHub();
        delivery.arriveAtDestHub();

        // When
        delivery.startVendorDelivery();

        // Then
        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.VENDOR_IN_PROGRESS);
    }

    @Test
    @DisplayName("배송 완료 - 성공")
    void completeDelivery_Success() {
        // Given
        Delivery delivery = createTestDelivery();
        delivery.startFromHub();
        delivery.arriveAtDestHub();
        delivery.startVendorDelivery();

        // When
        delivery.completeDelivery();

        // Then
        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.DELIVERED);
    }

    @Test
    @DisplayName("업체 배송담당자 배정 - 성공")
    void assignVendorDeliveryPerson_Success() {
        // Given
        Delivery delivery = createTestDelivery();
        delivery.startFromHub();
        delivery.arriveAtDestHub();
        Long vendorPersonId = 100L;

        // When
        delivery.assignVendorDeliveryPerson(vendorPersonId);

        // Then
        assertThat(delivery.getVendorDeliveryPersonId()).isEqualTo(vendorPersonId);
    }

    @Test
    @DisplayName("목적지 허브 도착 전 업체 배송담당자 배정 불가 - 실패")
    void assignVendorDeliveryPerson_BeforeDestArrival_Fail() {
        // Given
        Delivery delivery = createTestDelivery();
        Long vendorPersonId = 100L;

        // When & Then
        assertThatThrownBy(() -> delivery.assignVendorDeliveryPerson(vendorPersonId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("목적지 허브 도착 후에만 업체 배송담당자를 배정할 수 있습니다.");
    }

    @Test
    @DisplayName("배송 취소 - 성공")
    void markDeleted_Success() {
        // Given
        Delivery delivery = createTestDelivery();
        String deletedBy = "admin";

        // When
        delivery.markDeleted(deletedBy);

        // Then
        assertThat(delivery.isDeleted()).isTrue();
        assertThat(delivery.getDeletedAt()).isNotNull();
        assertThat(delivery.getDeletedBy()).isEqualTo(deletedBy);
    }

    @Test
    @DisplayName("배송 시작 후 취소 불가 - 실패")
    void markDeleted_AfterStart_Fail() {
        // Given
        Delivery delivery = createTestDelivery();
        delivery.startFromHub();

        // When & Then
        assertThatThrownBy(() -> delivery.markDeleted("admin"))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("배송 시작 전에만 삭제할 수 있습니다.");
    }


}