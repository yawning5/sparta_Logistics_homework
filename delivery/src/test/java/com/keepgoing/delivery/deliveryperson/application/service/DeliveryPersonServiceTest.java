package com.keepgoing.delivery.deliveryperson.application.service;

import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPerson;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPersonType;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliverySeq;
import com.keepgoing.delivery.deliveryperson.domain.repository.DeliveryPersonRepository;
import com.keepgoing.delivery.deliveryperson.domain.service.DeliveryPersonDomainService;
import com.keepgoing.delivery.global.exception.BusinessException;
import com.keepgoing.delivery.global.exception.ErrorCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DeliveryPersonServiceTest {

    @Mock
    private DeliveryPersonRepository deliveryPersonRepository;

    @Mock
    private DeliveryPersonDomainService deliveryPersonDomainService;

    @InjectMocks
    private DeliveryPersonService deliveryPersonService;

    private final String MASTER_ROLE = "MASTER";
    private final String HUB_MANAGER_ROLE = "HUB";
    private final UUID HUB_ID_A = UUID.randomUUID();
    private final UUID HUB_ID_B = UUID.randomUUID();

    @Test
    @DisplayName("배송 담당자 등록 - MASTER 성공")
    void registerDeliveryPerson_Master_Success() {
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
        DeliveryPerson result = deliveryPersonService.registerDeliveryPerson(
                userId, slackId, type, hubId, MASTER_ROLE, null);

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
                deliveryPersonService.registerDeliveryPerson(
                        userId, "@new", DeliveryPersonType.HUB, null, MASTER_ROLE, null)
        )
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DELIVERY_PERSON_ALREADY_REGISTERED);

        verify(deliveryPersonRepository, never()).save(any());
    }

    @Test
    @DisplayName("배송 담당자 조회 - 성공")
    void findDeliveryPerson_Success() {
        // Given
        Long userId = 1L;
        Long masterId = 2L;

        DeliveryPerson person = DeliveryPerson.createHubDeliveryPerson(
                userId, "@user", new DeliverySeq(1)
        );

        given(deliveryPersonRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(person));

        // When
        DeliveryPerson result = deliveryPersonService.findDeliveryPerson(userId, masterId, MASTER_ROLE);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("배송 담당자 조회 with 권한 - MASTER 성공")
    void findDeliveryPersonWithAuth_Master_Success() {
        // Given
        Long userId = 1L;
        DeliveryPerson person = DeliveryPerson.createVendorDeliveryPerson(
                userId, HUB_ID_A, "@user", new DeliverySeq(1)
        );

        given(deliveryPersonRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(person));

        // When
        DeliveryPerson result = deliveryPersonService.findDeliveryPersonWithAuth(
                userId, MASTER_ROLE, null);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("배송 담당자 조회 with 권한 - HUB_MANAGER 자기 허브 성공")
    void findDeliveryPersonWithAuth_HubManager_OwnHub_Success() {
        // Given
        Long userId = 1L;
        DeliveryPerson person = DeliveryPerson.createVendorDeliveryPerson(
                userId, HUB_ID_A, "@user", new DeliverySeq(1)
        );

        given(deliveryPersonRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(person));

        // When
        DeliveryPerson result = deliveryPersonService.findDeliveryPersonWithAuth(
                userId, HUB_MANAGER_ROLE, HUB_ID_A);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
    }

    @Test
    @DisplayName("배송 담당자 조회 with 권한 - HUB_MANAGER 다른 허브 실패")
    void findDeliveryPersonWithAuth_HubManager_OtherHub_Fail() {
        // Given
        Long userId = 1L;
        DeliveryPerson person = DeliveryPerson.createVendorDeliveryPerson(
                userId, HUB_ID_B, "@user", new DeliverySeq(1)
        );

        given(deliveryPersonRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(person));

        // When & Then
        assertThatThrownBy(() ->
                deliveryPersonService.findDeliveryPersonWithAuth(userId, HUB_MANAGER_ROLE, HUB_ID_A)
        )
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.FORBIDDEN_HUB_ACCESS);
    }

    @Test
    @DisplayName("배송 담당자 조회 - 존재하지 않음")
    void findDeliveryPerson_NotFound_Fail() {
        // Given
        Long userId = 999L;
        Long masterId = 1L;
        given(deliveryPersonRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> deliveryPersonService.findDeliveryPerson(userId, masterId, MASTER_ROLE))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DELIVERY_PERSON_NOT_FOUND);
    }

    @Test
    @DisplayName("Slack ID 수정 - MASTER 성공")
    void updateSlackId_Master_Success() {
        // Given
        Long userId = 1L;
        String newSlackId = "@new_user";
        DeliveryPerson person = DeliveryPerson.createVendorDeliveryPerson(
                userId, HUB_ID_A, "@old_user", new DeliverySeq(1));

        given(deliveryPersonRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(person));

        // When
        deliveryPersonService.updateSlackId(userId, newSlackId, MASTER_ROLE, null);

        // Then
        verify(deliveryPersonRepository).findByIdAndIsDeletedFalse(userId);
        assertThat(person.getSlackId()).isEqualTo(newSlackId);
    }

    @Test
    @DisplayName("Slack ID 수정 - HUB_MANAGER 다른 허브 실패")
    void updateSlackId_HubManager_OtherHub_Fail() {
        // Given
        Long userId = 1L;
        String newSlackId = "@new_user";
        DeliveryPerson person = DeliveryPerson.createVendorDeliveryPerson(
                userId, HUB_ID_B, "@old_user", new DeliverySeq(1));

        given(deliveryPersonRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(person));

        // When & Then
        assertThatThrownBy(() ->
                deliveryPersonService.updateSlackId(userId, newSlackId, HUB_MANAGER_ROLE, HUB_ID_A)
        )
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.FORBIDDEN_HUB_ACCESS);
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

        // When
        deliveryPersonService.changeType(userId, DeliveryPersonType.VENDOR, newHubId,
                MASTER_ROLE, null);

        // Then
        verify(deliveryPersonRepository).findByIdAndIsDeletedFalse(userId);
        assertThat(person.getType()).isEqualTo(DeliveryPersonType.VENDOR);
        assertThat(person.getHubId()).isEqualTo(newHubId);
    }

    @Test
    @DisplayName("배송 담당자 삭제 - MASTER 성공")
    void softDelete_Master_Success() {
        // Given
        Long userId = 1L;
        Long deletedBy = 2L;
        DeliveryPerson person = DeliveryPerson.createVendorDeliveryPerson(
                userId, HUB_ID_A, "@user", new DeliverySeq(1));

        given(deliveryPersonRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(person));

        // When
        deliveryPersonService.softDelete(userId, deletedBy, MASTER_ROLE, null);

        // Then
        verify(deliveryPersonRepository).findByIdAndIsDeletedFalse(userId);
        assertThat(person.isDeleted()).isTrue();
        assertThat(person.getDeletedBy()).isEqualTo(deletedBy);
    }

    @Test
    @DisplayName("배송 담당자 삭제 - HUB_MANAGER 다른 허브 실패")
    void softDelete_HubManager_OtherHub_Fail() {
        // Given
        Long userId = 1L;
        Long deletedBy = 2L;
        DeliveryPerson person = DeliveryPerson.createVendorDeliveryPerson(
                userId, HUB_ID_B, "@user", new DeliverySeq(1));

        given(deliveryPersonRepository.findByIdAndIsDeletedFalse(userId))
                .willReturn(Optional.of(person));

        // When & Then
        assertThatThrownBy(() ->
                deliveryPersonService.softDelete(userId, deletedBy, HUB_MANAGER_ROLE, HUB_ID_A)
        )
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.FORBIDDEN_HUB_ACCESS);
    }
}