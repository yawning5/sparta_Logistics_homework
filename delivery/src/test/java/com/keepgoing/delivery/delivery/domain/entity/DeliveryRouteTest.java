package com.keepgoing.delivery.delivery.domain.entity;

import com.keepgoing.delivery.delivery.domain.vo.Distance;
import com.keepgoing.delivery.delivery.domain.vo.Duration;
import com.keepgoing.delivery.delivery.domain.vo.RouteSeq;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class DeliveryRouteTest {

    @Test
    @DisplayName("배송 경로 생성 - 성공")
    void createDeliveryRoute_Success() {
        // Given
        UUID deliveryId = UUID.randomUUID();
        UUID departureHubId = UUID.randomUUID();
        UUID arrivalHubId = UUID.randomUUID();
        Distance expectedDistance = new Distance(100.0);
        Duration expectedTime = new Duration(120);
        RouteSeq routeSeq = new RouteSeq(1);

        // When
        DeliveryRoute route = DeliveryRoute.create(
                deliveryId, departureHubId, arrivalHubId, expectedDistance, expectedTime, routeSeq
        );

        // Then
        assertThat(route.getId()).isNotNull();
        assertThat(route.getDeliveryId()).isEqualTo(deliveryId);
        assertThat(route.getStatus()).isEqualTo(DeliveryRouteStatus.WAITING);
        assertThat(route.getExpectedDistance()).isEqualTo(expectedDistance);
        assertThat(route.getExpectedTime()).isEqualTo(expectedTime);
    }

    @Test
    @DisplayName("필수값 누락 시 예외 발생 - 출발 허브 ID는 필수")
    void createRoute_ShouldThrow_WhenDepartureHubIdIsNull() {
        assertThatThrownBy(() ->
                DeliveryRoute.create(null, UUID.randomUUID(), UUID.randomUUID(),
                        new Distance(10), new Duration(15), new RouteSeq(1))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발 허브 ID는 필수");
    }

    @Test
    @DisplayName("필수값 누락 시 예외 발생 - 도착 허브 ID는 필수")
    void createRoute_ShouldThrow_WhenArrivalHubIdIsNull() {
        assertThatThrownBy(() ->
                DeliveryRoute.create(UUID.randomUUID(), null, UUID.randomUUID(),
                        new Distance(10), new Duration(15), new RouteSeq(1))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("도착 허브 ID는 필수");
    }

    @Test
    @DisplayName("필수값 누락 시 예외 발생 - 거리와 시간은 필수")
    void createRoute_ShouldThrow_WhenDistanceOrDurationIsNull() {
        UUID deliveryId = UUID.randomUUID();
        UUID departureHubId = UUID.randomUUID();
        UUID arrivalHubId = UUID.randomUUID();

        assertThatThrownBy(() ->
                DeliveryRoute.create(deliveryId, departureHubId, arrivalHubId, null, new Duration(10), new RouteSeq(1))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("거리와 소요 시간은 필수");

        assertThatThrownBy(() ->
                DeliveryRoute.create(deliveryId, departureHubId, arrivalHubId, new Distance(10), null, new RouteSeq(1))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("거리와 소요 시간은 필수");
    }

    @Test
    @DisplayName("배송담당자 배정 - 성공")
    void assignDeliveryPerson_Success() {
        // Given
        DeliveryRoute route = createTestRoute();
        Long deliveryPersonId = 100L;

        // When
        route.assignDeliveryPerson(deliveryPersonId);

        // Then
        assertThat(route.getDeliveryPersonId()).isEqualTo(deliveryPersonId);
    }

    @Test
    @DisplayName("이미 배정된 경로에 재배정 불가 - 실패")
    void assignDeliveryPerson_AlreadyAssigned_Fail() {
        // Given
        DeliveryRoute route = createTestRoute();
        route.assignDeliveryPerson(100L);

        // When & Then
        assertThatThrownBy(() -> route.assignDeliveryPerson(200L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 배달원이 지정되어 있습니다.");
    }

    @Test
    @DisplayName("실제 거리/시간 기록 - 성공")
    void recordActual_Success() {
        // Given
        DeliveryRoute route = createTestRoute();
        route.assignDeliveryPerson(100L);

        // WAITING -> MOVING -> ARRIVED 상태 변경 (리플렉션 사용)
        try {
            var statusField = DeliveryRoute.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(route, DeliveryRouteStatus.MOVING);
            statusField.set(route, DeliveryRouteStatus.ARRIVED);
        } catch (Exception e) {
            fail("상태 변경 실패");
        }

        Distance actualDistance = new Distance(105.0);
        Duration actualTime = new Duration(130);

        // When
        route.recordActual(actualDistance, actualTime);

        // Then
        assertThat(route.getActualDistance()).isEqualTo(actualDistance);
        assertThat(route.getActualTime()).isEqualTo(actualTime);
    }

    @Test
    @DisplayName("ARRIVED 상태가 아닐 때 실제값 기록 불가 - 실패")
    void recordActual_NotArrived_Fail() {
        // Given
        DeliveryRoute route = createTestRoute();
        Distance actualDistance = new Distance(105.0);
        Duration actualTime = new Duration(130);

        // When & Then
        assertThatThrownBy(() -> route.recordActual(actualDistance, actualTime))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("ARRIVED 상태에서만 실제 거리/시간을 기록할 수 있습니다.");
    }

    private DeliveryRoute createTestRoute() {
        return DeliveryRoute.create(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Distance(100.0),
                new Duration(120),
                new RouteSeq(1)
        );
    }
}