package com.keepgoing.delivery.deliveryperson.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class DeliveryPersonDomainTest {

    @Test
    @DisplayName("DeliverySeq 유효성: 1보다 작은 값은 예외 발생")
    void deliverySeq_Invalid_ShouldThrow() {
        // given & when & then
        assertThatThrownBy(() -> new DeliverySeq(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("DeliverySeq must be between 1 and 10");
    }

    @Test
    @DisplayName("DeliverySeq 유효성: 10 초과 값은 예외")
    void deliverySeq_TooLarge_ShouldThrow() {
        // given & when & then
        assertThatThrownBy(() -> new DeliverySeq(11))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("DeliverySeq must be between 1 and 10");
    }


    @Test
    @DisplayName("DeliveryPerson 생성 성공 - 기본 필수값 검증")
    void createDeliveryPerson_Success() {
        // given
        UUID hubId = UUID.randomUUID();

        // when
        DeliveryPerson person = DeliveryPerson.createVendorDeliveryPerson(
                100L,
                hubId,
                "SLACK-100",
                new DeliverySeq(1)
        );

        // then
        assertThat(person.getUserId()).isEqualTo(100L);
        assertThat(person.getHubId()).isEqualTo(hubId);
        assertThat(person.getSlackId()).isEqualTo("SLACK-100");
        assertThat(person.getType()).isEqualTo(DeliveryPersonType.VENDOR);
        assertThat(person.getDeliverySeq().value()).isEqualTo(1);
    }

    @Test
    @DisplayName("허브 배송 담당자는 hubId 없이 생성 가능하다")
    void createHubDeliveryPerson_WithoutHubId_Success() {
        // given & when
        DeliveryPerson person = DeliveryPerson.createHubDeliveryPerson(
                200L,
                "SLACK-200",
                new DeliverySeq(1)
        );

        // then
        assertThat(person.getHubId()).isNull();
        assertThat(person.getType()).isEqualTo(DeliveryPersonType.HUB);
    }

    @Test
    @DisplayName("업체 배송 담당자는 hubId 없으면 예외 발생")
    void createVendorDeliveryPerson_WithoutHubId_ShouldThrow() {
        // given & when & then
        assertThatThrownBy(() -> DeliveryPerson.createVendorDeliveryPerson(
                300L,
                null,
                "SLACK-300",
                new DeliverySeq(1))
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("업체 배송 담당자의 hubId는 필수입니다.");
    }

    @Test
    @DisplayName("허브 재배정: assignToHub 호출 시 hubId 변경")
    void assignToHub_ShouldChangeHubId() {
        // given
        UUID oldHub = UUID.randomUUID();
        DeliveryPerson person = DeliveryPerson.createVendorDeliveryPerson(
                400L,
                oldHub,
                "SLACK-400",
                new DeliverySeq(1)
        );

        UUID newHub = UUID.randomUUID();

        // when
        person.assignToHub(newHub);

        // then
        assertThat(person.getHubId()).isEqualTo(newHub);
    }
}
