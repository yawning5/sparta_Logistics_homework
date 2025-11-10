package com.keepgoing.delivery.deliveryperson.domain.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class DeliveryPersonTest {

    @Test
    @DisplayName("허브 배송 담당자 생성 - 성공")
    void createHubDeliveryPerson_Success() {
        // Given
        Long userId = 1L;
        String slackId = "@hub_person";
        DeliverySeq seq = new DeliverySeq(1);

        // When
        DeliveryPerson person = DeliveryPerson.createHubDeliveryPerson(userId, slackId, seq);

        // Then
        assertThat(person.getId()).isEqualTo(userId);
        assertThat(person.getSlackId()).isEqualTo(slackId);
        assertThat(person.getType()).isEqualTo(DeliveryPersonType.HUB);
        assertThat(person.getHubId()).isNull();
        assertThat(person.getDeliverySeq()).isEqualTo(seq);
    }

    @Test
    @DisplayName("DeliverySeq 유효성: 1보다 작은 값은 예외 발생")
    void deliverySeq_Invalid_ShouldThrow() {
        // given & when & then
        assertThatThrownBy(() -> new DeliverySeq(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("DeliverySeq must be between 1 and 10");
    }


    @Test
    @DisplayName("업체 배송 담당자 생성 - 성공")
    void createVendorDeliveryPerson_Success() {
        // Given
        Long userId = 2L;
        UUID hubId = UUID.randomUUID();
        String slackId = "@vendor_person";
        DeliverySeq seq = new DeliverySeq(1);

        // When
        DeliveryPerson person = DeliveryPerson.createVendorDeliveryPerson(userId, hubId, slackId, seq);

        // Then
        assertThat(person.getId()).isEqualTo(userId);
        assertThat(person.getSlackId()).isEqualTo(slackId);
        assertThat(person.getType()).isEqualTo(DeliveryPersonType.VENDOR);
        assertThat(person.getHubId()).isEqualTo(hubId);
        assertThat(person.getDeliverySeq()).isEqualTo(seq);
    }

    @Test
    @DisplayName("업체 배송 담당자 생성 시 hubId 필수 - 실패")
    void createVendorDeliveryPerson_WithoutHubId_Fail() {
        // Given
        Long userId = 2L;
        String slackId = "@vendor_person";
        DeliverySeq seq = new DeliverySeq(1);

        // When & Then
        assertThatThrownBy(() ->
                DeliveryPerson.createVendorDeliveryPerson(userId, null, slackId, seq)
        )
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("업체 배송 담당자의 hubId는 필수입니다.");
    }

    @Test
    @DisplayName("Slack ID 수정 - 성공")
    void updateSlackId_Success() {
        // Given
        DeliveryPerson person = DeliveryPerson.createHubDeliveryPerson(1L, "@old_id", new DeliverySeq(1));
        String newSlackId = "@new_id";

        // When
        person.updateSlackId(newSlackId);

        // Then
        assertThat(person.getSlackId()).isEqualTo(newSlackId);
    }

    @Test
    @DisplayName("Slack ID를 null로 수정 - 실패")
    void updateSlackId_WithNull_Fail() {
        // Given
        DeliveryPerson person = DeliveryPerson.createHubDeliveryPerson(1L, "@old_id", new DeliverySeq(1));

        // When & Then
        assertThatThrownBy(() -> person.updateSlackId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("slackId는 필수입니다.");
    }

    @Test
    @DisplayName("업체 배송 담당자 허브 변경 - 성공")
    void changeHub_Success() {
        // Given
        UUID oldHubId = UUID.randomUUID();
        UUID newHubId = UUID.randomUUID();
        DeliveryPerson person = DeliveryPerson.createVendorDeliveryPerson(
                1L, oldHubId, "@vendor", new DeliverySeq(1)
        );

        // When
        person.changeHub(newHubId);

        // Then
        assertThat(person.getHubId()).isEqualTo(newHubId);
    }

    @Test
    @DisplayName("허브 배송 담당자는 허브 변경 불가 - 실패")
    void changeHub_HubPerson_Fail() {
        // Given
        DeliveryPerson person = DeliveryPerson.createHubDeliveryPerson(1L, "@hub", new DeliverySeq(1));
        UUID newHubId = UUID.randomUUID();

        // When & Then
        assertThatThrownBy(() -> person.changeHub(newHubId))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("업체 배송 담당자만 허브를 변경할 수 있습니다.");
    }

    @Test
    @DisplayName("타입 변경: HUB -> VENDOR - 성공")
    void changeType_HubToVendor_Success() {
        // Given
        DeliveryPerson person = DeliveryPerson.createHubDeliveryPerson(1L, "@hub", new DeliverySeq(1));
        UUID newHubId = UUID.randomUUID();

        // When
        person.changeType(DeliveryPersonType.VENDOR, newHubId);

        // Then
        assertThat(person.getType()).isEqualTo(DeliveryPersonType.VENDOR);
        assertThat(person.getHubId()).isEqualTo(newHubId);
    }

    @Test
    @DisplayName("타입 변경: VENDOR -> HUB - 성공")
    void changeType_VendorToHub_Success() {
        // Given
        UUID hubId = UUID.randomUUID();
        DeliveryPerson person = DeliveryPerson.createVendorDeliveryPerson(
                1L, hubId, "@vendor", new DeliverySeq(1)
        );

        // When
        person.changeType(DeliveryPersonType.HUB, null);

        // Then
        assertThat(person.getType()).isEqualTo(DeliveryPersonType.HUB);
        assertThat(person.getHubId()).isNull();
    }

    @Test
    @DisplayName("HUB -> VENDOR 변경 시 hubId 필수 - 실패")
    void changeType_HubToVendor_WithoutHubId_Fail() {
        // Given
        DeliveryPerson person = DeliveryPerson.createHubDeliveryPerson(1L, "@hub", new DeliverySeq(1));

        // When & Then
        assertThatThrownBy(() -> person.changeType(DeliveryPersonType.VENDOR, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("업체 배송 담당자로 전환 시 소속 허브는 필수입니다.");
    }

    @Test
    @DisplayName("동일한 타입으로 변경 불가 - 실패")
    void changeType_SameType_Fail() {
        // Given
        DeliveryPerson person = DeliveryPerson.createHubDeliveryPerson(1L, "@hub", new DeliverySeq(1));

        // When & Then
        assertThatThrownBy(() -> person.changeType(DeliveryPersonType.HUB, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("동일한 타입으로는 변경할 수 없습니다.");
    }

    @Test
    @DisplayName("배송 담당자 삭제 - 성공")
    void markDeleted_Success() {
        // Given
        DeliveryPerson person = DeliveryPerson.createHubDeliveryPerson(1L, "@hub", new DeliverySeq(1));
        String deletedBy = "admin";

        // When
        person.markDeleted(deletedBy);

        // Then
        assertThat(person.isDeleted()).isTrue();
        assertThat(person.getDeletedAt()).isNotNull();
        assertThat(person.getDeletedBy()).isEqualTo(deletedBy);
    }

    @Test
    @DisplayName("이미 삭제된 배송 담당자 재삭제 - 실패")
    void markDeleted_AlreadyDeleted_Fail() {
        // Given
        DeliveryPerson person = DeliveryPerson.createHubDeliveryPerson(1L, "@hub", new DeliverySeq(1));
        person.markDeleted("admin");

        // When & Then
        assertThatThrownBy(() -> person.markDeleted("admin2"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 삭제된 배송 담당자입니다.");
    }
}