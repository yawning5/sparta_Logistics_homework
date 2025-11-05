package com.keepgoing.delivery.delivery.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class DeliveryRouteDomainTest {

    @Test
    @DisplayName("필수값 누락 시 예외 발생 - 출발 허브 ID는 필수")
    void createRoute_ShouldThrow_WhenDepartureHubIdIsNull() {
        assertThatThrownBy(() ->
                DeliveryRoute.create(null, UUID.randomUUID(),
                        new Distance(10), new Duration(15), new RouteSeq(1))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("출발 허브 ID는 필수");
    }

    @Test
    @DisplayName("필수값 누락 시 예외 발생 - 도착 허브 ID는 필수")
    void createRoute_ShouldThrow_WhenArrivalHubIdIsNull() {
        assertThatThrownBy(() ->
                DeliveryRoute.create(UUID.randomUUID(), null,
                        new Distance(10), new Duration(15), new RouteSeq(1))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("도착 허브 ID는 필수");
    }

    @Test
    @DisplayName("필수값 누락 시 예외 발생 - 거리와 시간은 필수")
    void createRoute_ShouldThrow_WhenDistanceOrDurationIsNull() {
        UUID depHub = UUID.randomUUID();
        UUID arrHub = UUID.randomUUID();

        assertThatThrownBy(() ->
                DeliveryRoute.create(depHub, arrHub, null, new Duration(10), new RouteSeq(1))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("거리와 소요 시간은 필수");

        assertThatThrownBy(() ->
                DeliveryRoute.create(depHub, arrHub, new Distance(10), null, new RouteSeq(1))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("거리와 소요 시간은 필수");
    }

    @Test
    @DisplayName("정상 생성 시 기본 상태는 WAITING 이어야 한다")
    void createRoute_ShouldSetDefaultStatus() {
        UUID depHub = UUID.randomUUID();
        UUID arrHub = UUID.randomUUID();

        DeliveryRoute route = DeliveryRoute.create(depHub, arrHub,
                new Distance(12), new Duration(30), new RouteSeq(1));

        assertThat(route.getStatus()).isEqualTo(DeliveryRouteStatus.WAITING);
    }
}
