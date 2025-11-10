package com.keepgoing.delivery.deliveryperson.application.service;

import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPerson;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPersonType;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliverySeq;
import com.keepgoing.delivery.deliveryperson.domain.repository.DeliveryPersonRepository;
import com.keepgoing.delivery.deliveryperson.domain.service.DeliveryPersonDomainService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryPersonServiceTest {

    @Mock
    private DeliveryPersonRepository deliveryPersonRepository;

    @Mock
    private DeliveryPersonDomainService deliveryPersonDomainService;

    @InjectMocks
    private DeliveryPersonService deliveryPersonService;

    @Test
    @DisplayName("배송 담당자 등록 - 성공")
    void registerDeliveryPerson_Success() {
        // Given
        Long userId = 1L;
        String slackId = "@user123";
        DeliveryPersonType type = DeliveryPersonType.HUB;
        UUID hubId = null;

        given(deliveryPersonRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.empty());
        given(deliveryPersonRepository.countByTypeAndHubId(type, hubId))
                .willReturn(0);
        given(deliveryPersonRepository.findMaxSeqByTypeAndHubId(type, hubId))
                .willReturn(0);
        given(deliveryPersonDomainService.assignNextSeq(0, 0))
                .willReturn(new DeliverySeq(1));

        DeliveryPerson expected = DeliveryPerson.createHubDeliveryPerson(userId, slackId, new DeliverySeq(1));
        given(deliveryPersonRepository.save(any(DeliveryPerson.class)))
                .willReturn(expected);

        // When
        DeliveryPerson result = deliveryPersonService.registerDeliveryPerson(userId, slackId, type, hubId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getType()).isEqualTo(type);
        verify(deliveryPersonRepository).save(any(DeliveryPerson.class));
    }

    @Test
    @DisplayName("배송 담당자 중복 등록 - 실패")
    void registerDeliveryPerson_AlreadyExists_Fail() {
        // Given
        Long userId = 1L;
        DeliveryPerson existing = DeliveryPerson.createHubDeliveryPerson(
                userId, "@existing", new DeliverySeq(1)
        );

        given(deliveryPersonRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(existing));

        // When & Then
        assertThatThrownBy(() ->
                deliveryPersonService.registerDeliveryPerson(userId, "@new", DeliveryPersonType.HUB, null)
        )
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 등록된 배송 담당자입니다.");

        verify(deliveryPersonRepository, never()).save(any());
    }

    @Test
    @DisplayName("배송 담당자 조회 - 성공")
    void findDeliveryPerson_Success() {
        // Given
        Long userId = 1L;
        DeliveryPerson person = DeliveryPerson.createHubDeliveryPerson(
                userId, "@user", new DeliverySeq(1)
        );

        given(deliveryPersonRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(person));

        // When
        DeliveryPerson result = deliveryPersonService.findDeliveryPerson(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("배송 담당자 조회 - 존재하지 않음")
    void findDeliveryPerson_NotFound_Fail() {
        // Given
        Long userId = 999L;
        given(deliveryPersonRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> deliveryPersonService.findDeliveryPerson(userId))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("배송 담당자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("타입별 배송 담당자 조회 - 성공")
    void findDeliveryPersonsByType_Success() {
        // Given
        DeliveryPerson person1 = DeliveryPerson.createHubDeliveryPerson(1L, "@user1", new DeliverySeq(1));
        DeliveryPerson person2 = DeliveryPerson.createHubDeliveryPerson(2L, "@user2", new DeliverySeq(2));

        given(deliveryPersonRepository.findByTypeAndIsDeletedFalse(DeliveryPersonType.HUB))
                .willReturn(List.of(person1, person2));

        // When
        List<DeliveryPerson> result = deliveryPersonService.findDeliveryPersonsByType(DeliveryPersonType.HUB);

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).extracting(DeliveryPerson::getType)
                .containsOnly(DeliveryPersonType.HUB);
    }

    @Test
    @DisplayName("Slack ID 수정 - 성공")
    void updateSlackId_Success() {
        // Given
        Long userId = 1L;
        String newSlackId = "@new_user";
        DeliveryPerson person = DeliveryPerson.createHubDeliveryPerson(userId, "@old_user", new DeliverySeq(1));

        given(deliveryPersonRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(person));
        given(deliveryPersonRepository.save(any(DeliveryPerson.class)))
                .willReturn(person);

        // When
        deliveryPersonService.updateSlackId(userId, newSlackId);

        // Then
        verify(deliveryPersonRepository).save(person);
        assertThat(person.getSlackId()).isEqualTo(newSlackId);
    }

    @Test
    @DisplayName("타입 변경 - 성공")
    void changeType_Success() {
        // Given
        Long userId = 1L;
        UUID newHubId = UUID.randomUUID();
        DeliveryPerson person = DeliveryPerson.createHubDeliveryPerson(userId, "@user", new DeliverySeq(1));

        given(deliveryPersonRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(person));
        given(deliveryPersonRepository.save(any(DeliveryPerson.class)))
                .willReturn(person);

        // When
        deliveryPersonService.changeType(userId, DeliveryPersonType.VENDOR, newHubId);

        // Then
        verify(deliveryPersonRepository).save(person);
        assertThat(person.getType()).isEqualTo(DeliveryPersonType.VENDOR);
        assertThat(person.getHubId()).isEqualTo(newHubId);
    }

    @Test
    @DisplayName("배송 담당자 삭제 - 성공")
    void softDelete_Success() {
        // Given
        Long userId = 1L;
        String deletedBy = "admin";
        DeliveryPerson person = DeliveryPerson.createHubDeliveryPerson(userId, "@user", new DeliverySeq(1));

        given(deliveryPersonRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(person));
        given(deliveryPersonRepository.save(any(DeliveryPerson.class)))
                .willReturn(person);

        // When
        deliveryPersonService.softDelete(userId, deletedBy);

        // Then
        verify(deliveryPersonRepository).save(person);
        assertThat(person.isDeleted()).isTrue();
        assertThat(person.getDeletedBy()).isEqualTo(deletedBy);
    }
}