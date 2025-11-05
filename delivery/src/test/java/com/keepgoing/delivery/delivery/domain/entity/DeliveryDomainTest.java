package com.keepgoing.delivery.delivery.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class DeliveryDomainTest {

    @Test
    @DisplayName("Address 유효성 검사 - 필수 필드 누락 시 예외 발생")
    void address_Invalid_ShouldThrow() {
        assertThatThrownBy(() -> new Address("", "Seoul", "12345"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("도로 명 주소 필수 입력");

        assertThatThrownBy(() -> new Address("Gangnam", " ", "12345"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("지번 주소 필수 입력");

        assertThatThrownBy(() -> new Address("Gangnam", "Seoul", ""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("우편 번호 필수 입력");
    }

    @Test
    @DisplayName("Distance 유효성 검사 - 음수 거리 예외 발생")
    void distance_Invalid_ShouldThrow() {
        assertThatThrownBy(() -> new Distance(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("거리는 0 이상입니다.");
    }

    @Test
    @DisplayName("Duration 유효성 검사 - 음수 시간 예외 발생")
    void duration_Invalid_ShouldThrow() {
        assertThatThrownBy(() -> new Duration(-5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("소요 시간은 0분 이상입니다.");
    }

    @Test
    @DisplayName("RouteSeq 유효성 검사 - 1 미만 예외 발생")
    void routeSeq_Invalid_ShouldThrow() {
        assertThatThrownBy(() -> new RouteSeq(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("경로 순서는 1부터 시작합니다.");
    }

    @Test
    @DisplayName("Delivery 정상 생성 시 기본 상태는 HUB_WAITING 이어야 한다")
    void delivery_DefaultStatus_ShouldBeHubWaiting() {
        // given
        UUID orderId = UUID.randomUUID();
        UUID departureHubId = UUID.randomUUID();
        UUID destinationHubId = UUID.randomUUID();
        Address address = new Address("Gangnam", "Seoul", "12345");
        Long recipientUserId = 1L;
        String slackId = "SLACK-001";

        // when
        Delivery delivery = Delivery.create(
                orderId,
                departureHubId,
                destinationHubId,
                address,
                recipientUserId,
                slackId
        );

        // then
        assertThat(delivery.getStatus()).isEqualTo(DeliveryStatus.HUB_WAITING);
        assertThat(delivery.getRoutes()).isEmpty();
        assertThat(delivery.getOrderId()).isEqualTo(orderId);
    }


    @Test
    @DisplayName("Delivery에 DeliveryRoute 추가 - 정상적으로 목록에 포함되어야 함")
    void delivery_AddRoute_ShouldSucceed() {
        // given
        Delivery delivery = Delivery.create(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Address("서울 강남구", "삼성동 123", "12345"),
                100L,
                "SLACK-100"
        );

        DeliveryRoute route = DeliveryRoute.create(
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Distance(100),
                new Duration(30),
                new RouteSeq(1)
        );

        // when
        delivery.addRoute(route);

        // then
        assertThat(delivery.getRoutes()).hasSize(1);
        assertThat(delivery.getRoutes().getFirst()).isEqualTo(route);
    }

    @Test
    @DisplayName("Delivery에 null Route 추가 시 예외 발생")
    void delivery_AddNullRoute_ShouldThrow() {
        // given
        Delivery delivery = Delivery.create(
                UUID.randomUUID(),
                UUID.randomUUID(),
                UUID.randomUUID(),
                new Address("서울 강남구", "삼성동 123", "12345"),
                100L,
                "SLACK-100"
        );

        // when & then
        assertThatThrownBy(() -> delivery.addRoute(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("경로 정보는 null일 수 없습니다");
    }

}
