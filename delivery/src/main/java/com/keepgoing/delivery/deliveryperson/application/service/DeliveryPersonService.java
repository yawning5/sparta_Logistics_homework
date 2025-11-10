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
            UUID hubId
    ) {
        // 중복 등록 방지
        if (deliveryPersonRepository.findByIdAndIsDeletedFalse(userId).isPresent()) {
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_ALREADY_REGISTERED);
        }

        int currentPersonCount = deliveryPersonRepository.countByTypeAndHubId(type, hubId);
        int maxSeqValue = deliveryPersonRepository.findMaxSeqByTypeAndHubId(type, hubId);

        DeliverySeq deliverySeq = deliveryPersonDomainService.assignNextSeq(currentPersonCount, maxSeqValue);


        DeliveryPerson deliveryPerson = (type == DeliveryPersonType.HUB)
                ? DeliveryPerson.createHubDeliveryPerson(userId, slackId, deliverySeq)
                : DeliveryPerson.createVendorDeliveryPerson(userId, hubId, slackId, deliverySeq);
        return deliveryPersonRepository.save(deliveryPerson);
    }

    // 배송 담당자 조회
    @Transactional(readOnly = true)
    public DeliveryPerson findDeliveryPerson(Long id) {
        return deliveryPersonRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_PERSON_NOT_FOUND));
    }

    // 배송 담당자 타입 별 조회
    @Transactional(readOnly = true)
    public List<DeliveryPerson> findDeliveryPersonsByType(DeliveryPersonType type) {
        return deliveryPersonRepository.findByTypeAndIsDeletedFalse(type);
    }

    // 허브 별 업체 배송 담당자 조회
    @Transactional(readOnly = true)
    public List<DeliveryPerson> findVendorDeliveryPersonsByHub(UUID hubId) {
        return deliveryPersonRepository.findByTypeAndHubIdAndIsDeletedFalse(
                DeliveryPersonType.VENDOR,
                hubId
        );
    }

    // slackId 수정
    public void updateSlackId(Long id, String slackId) {
        DeliveryPerson deliveryPerson = findDeliveryPerson(id);
        deliveryPerson.updateSlackId(slackId);
        deliveryPersonRepository.save(deliveryPerson);
    }

    // 업체 배송 담당자 소속 허브 변경
    public void changeHub(Long deliveryPersonId, UUID newHubId) {
        DeliveryPerson deliveryPerson = findDeliveryPerson(deliveryPersonId);
        deliveryPerson.changeHub(newHubId);
        deliveryPersonRepository.save(deliveryPerson);
    }

    // 배송 담당자 타입 변경
    public void changeType(Long deliveryPersonId, DeliveryPersonType newType, UUID newHubId) {
        DeliveryPerson deliveryPerson = findDeliveryPerson(deliveryPersonId);
        deliveryPerson.changeType(newType, newHubId);
        deliveryPersonRepository.save(deliveryPerson);
    }

    // 배송 담당자 삭제
    public void softDelete(Long id, String deletedBy) {
        DeliveryPerson deliveryPerson = findDeliveryPerson(id);
        deliveryPerson.markDeleted(deletedBy);
        deliveryPersonRepository.save(deliveryPerson);
    }
}