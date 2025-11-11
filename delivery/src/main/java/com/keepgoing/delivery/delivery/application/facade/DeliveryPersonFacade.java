package com.keepgoing.delivery.delivery.application.facade;

import com.keepgoing.delivery.deliveryperson.application.service.DeliveryPersonService;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPerson;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPersonType;
import com.keepgoing.delivery.global.exception.BusinessException;
import com.keepgoing.delivery.global.exception.ErrorCode;
import com.keepgoing.delivery.global.security.UserContextHolder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DeliveryPersonFacade {

    private final DeliveryPersonService deliveryPersonService;

    // 허브 배송담당자 목록 조회
    public List<DeliveryPerson> getHubDeliveryPersons() {
        return deliveryPersonService.findDeliveryPersonsByType(DeliveryPersonType.HUB);
    }

    // 특정 허브의 업체 배송담당자 목록 조회
    public List<DeliveryPerson> getVendorDeliveryPersonsByHub(UUID hubId, String userRole, UUID currentUserHubId) {
        return deliveryPersonService.findVendorDeliveryPersonsByHub(hubId,  userRole, currentUserHubId);
    }

    // 배송담당자 검증
    public void validateDeliveryPerson(Long userId, String expectedType, Long currentUserId, String userRole) {
        DeliveryPerson person = deliveryPersonService.findDeliveryPerson(userId, currentUserId, userRole);
        if (!person.getType().toString().equals(expectedType)) {
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_INVALID_TYPE);
        }
    }
}