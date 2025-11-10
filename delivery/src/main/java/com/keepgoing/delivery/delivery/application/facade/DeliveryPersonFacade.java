package com.keepgoing.delivery.delivery.application.facade;

import com.keepgoing.delivery.deliveryperson.application.service.DeliveryPersonService;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPerson;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPersonType;
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

    // 업체 배송담당자 목록 조회
    public List<DeliveryPerson> getVendorDeliveryPersons() {
        return deliveryPersonService.findDeliveryPersonsByType(DeliveryPersonType.VENDOR);
    }

    // 특정 허브의 업체 배송담당자 목록 조회
    public List<DeliveryPerson> getVendorDeliveryPersonsByHub(UUID hubId) {
        return deliveryPersonService.findVendorDeliveryPersonsByHub(hubId);
    }

    // 배송담당자 조회 (Id)
    public DeliveryPerson getDeliveryPerson(Long id) {
        return deliveryPersonService.findDeliveryPerson(id);
    }

    // 배송담당자 검증
    public void validateDeliveryPerson(Long userId, String expectedType) {
        DeliveryPerson person = deliveryPersonService.findDeliveryPerson(userId);
        if (!person.getType().toString().equals(expectedType)) {
            throw new IllegalArgumentException(
                    String.format("배송담당자 타입이 %s가 아닙니다.", expectedType)
            );
        }
    }
}