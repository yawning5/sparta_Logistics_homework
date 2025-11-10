package com.keepgoing.delivery.deliveryperson.application.dto.response;

import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPerson;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPersonType;

import java.util.UUID;

public record DeliveryPersonResponse(
        Long id,  // userId와 동일
        UUID hubId,
        String slackId,
        DeliveryPersonType type,
        int deliverySeq
) {
    public static DeliveryPersonResponse from(DeliveryPerson deliveryPerson) {
        return new DeliveryPersonResponse(
                deliveryPerson.getId(),
                deliveryPerson.getHubId(),
                deliveryPerson.getSlackId(),
                deliveryPerson.getType(),
                deliveryPerson.getDeliverySeq().value()
        );
    }
}