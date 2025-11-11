package com.keepgoing.delivery.deliveryperson.application.service;

import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPerson;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPersonType;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliverySeq;
import com.keepgoing.delivery.deliveryperson.domain.repository.DeliveryPersonRepository;
import com.keepgoing.delivery.deliveryperson.domain.service.DeliveryPersonDomainService;
import com.keepgoing.delivery.global.exception.BusinessException;
import com.keepgoing.delivery.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DeliveryPersonService {

    private final DeliveryPersonRepository deliveryPersonRepository;
    private final DeliveryPersonDomainService deliveryPersonDomainService;

    // 회원가입 시 배송 담당자 등록
    public DeliveryPerson registerDeliveryPerson(
            Long userId,
            String slackId,
            DeliveryPersonType type,
            UUID hubId,
            String userRole,
            UUID userHubId
    ) {
        // 중복 등록 방지
        if (deliveryPersonRepository.findByIdAndIsDeletedFalse(userId).isPresent()) {
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_ALREADY_REGISTERED);
        }

        // 등록 권한 검증
        validateRegistrationAuth(type, hubId, userRole, userHubId);

        // 배송 순서 할당
        int currentPersonCount = deliveryPersonRepository.countByTypeAndHubId(type, hubId);
        int maxSeqValue = deliveryPersonRepository.findMaxSeqByTypeAndHubId(type, hubId);
        DeliverySeq deliverySeq = deliveryPersonDomainService.assignNextSeq(currentPersonCount, maxSeqValue);

        // 배송담당자 생성
        DeliveryPerson deliveryPerson = (type == DeliveryPersonType.HUB)
                ? DeliveryPerson.createHubDeliveryPerson(userId, slackId, deliverySeq)
                : DeliveryPerson.createVendorDeliveryPerson(userId, hubId, slackId, deliverySeq);

        return deliveryPersonRepository.save(deliveryPerson);
    }

    // 등록 권한 검증
    private void validateRegistrationAuth(DeliveryPersonType type, UUID hubId,
                                          String userRole, UUID userHubId) {
        // MASTER는 모든 배송담당자 등록 가능
        if ("MASTER".equals(userRole)) {
            return;
        }

        // HUB_MANAGER는 자기 허브의 배송담당자만 등록 가능
        if ("HUB_MANAGER".equals(userRole)) {
            if (hubId == null || !hubId.equals(userHubId)) {
                throw new BusinessException(ErrorCode.FORBIDDEN_HUB_ACCESS);
            }
            return;
        }

        // 그 외 역할은 등록 불가
        throw new BusinessException(ErrorCode.USER_FORBIDDEN);
    }


    // 배송 담당자 조회 (권한 체크 없음 - 내부용)
    @Transactional(readOnly = true)
    public DeliveryPerson findDeliveryPerson(Long id, Long userId, String userRole) {
        DeliveryPerson deliveryPerson = deliveryPersonRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_PERSON_NOT_FOUND));

        if ("Delivery".equals(userRole)) {
            if (!deliveryPerson.getId().equals(userId)) {
                throw new BusinessException(ErrorCode.FORBIDDEN_HUB_ACCESS);
            }
        }

        return deliveryPerson;
    }

    // 배송 담당자 조회 with 권한 체크
    @Transactional(readOnly = true)
    public DeliveryPerson findDeliveryPersonWithAuth(Long id, String userRole, UUID userHubId) {
        DeliveryPerson person = deliveryPersonRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_PERSON_NOT_FOUND));

        // 권한 검증
        validateAccessAuth(person, userRole, userHubId);
        return person;
    }

    // 접근 권한 검증 (조회/수정/삭제 공통)
    private void validateAccessAuth(DeliveryPerson person, String userRole, UUID userHubId) {
        // MASTER는 모든 배송담당자 접근 가능
        if ("MASTER".equals(userRole)) {
            return;
        }

        // HUB_MANAGER는 자기 허브 소속만 접근 가능
        if ("HUB".equals(userRole)) {
            if (person.getHubId() == null || !person.getHubId().equals(userHubId)) {
                throw new BusinessException(ErrorCode.FORBIDDEN_HUB_ACCESS);
            }
            return;
        }

        throw new BusinessException(ErrorCode.USER_FORBIDDEN);
    }

    // 배송 담당자 타입 별 조회
    @Transactional(readOnly = true)
    public List<DeliveryPerson> findDeliveryPersonsByType(DeliveryPersonType type) {
        return deliveryPersonRepository.findByTypeAndIsDeletedFalse(type);
    }

    // 허브 별 업체 배송 담당자 조회
    @Transactional(readOnly = true)
    public List<DeliveryPerson> findVendorDeliveryPersonsByHub(UUID hubId, String userRole, UUID userHubId) {
        if ("HUB".equals(userRole)) {
            if (userHubId == null || !userHubId.equals(hubId)) {
                throw new BusinessException(ErrorCode.FORBIDDEN_HUB_ACCESS);
            }
        }
        return deliveryPersonRepository.findByTypeAndHubIdAndIsDeletedFalse(
                DeliveryPersonType.VENDOR,
                hubId
        );
    }

    // slackId 수정
    public void updateSlackId(Long id, String slackId, String userRole, UUID userHubId) {
        DeliveryPerson deliveryPerson = findDeliveryPersonWithAuth(id, userRole, userHubId);
        deliveryPerson.updateSlackId(slackId);
        deliveryPersonRepository.save(deliveryPerson);
    }

    // 업체 배송 담당자 소속 허브 변경
    public void changeHub(Long deliveryPersonId, UUID newHubId, String userRole, UUID userHubId) {
        DeliveryPerson deliveryPerson = findDeliveryPersonWithAuth(deliveryPersonId, userRole, userHubId);
        deliveryPerson.changeHub(newHubId);
        deliveryPersonRepository.save(deliveryPerson);
    }

    // 배송 담당자 타입 변경
    public void changeType(Long deliveryPersonId, DeliveryPersonType newType, UUID newHubId, String userRole, UUID userHubId) {
        DeliveryPerson deliveryPerson = findDeliveryPersonWithAuth(deliveryPersonId, userRole, userHubId);
        deliveryPerson.changeType(newType, newHubId);
        deliveryPersonRepository.save(deliveryPerson);
    }

    // 배송 담당자 삭제
    public void softDelete(Long id, Long currentUserId, String userRole, UUID userHubId) {
        DeliveryPerson deliveryPerson = findDeliveryPersonWithAuth(id, userRole, userHubId);
        deliveryPerson.markDeleted(currentUserId);
        deliveryPersonRepository.save(deliveryPerson);
    }
}