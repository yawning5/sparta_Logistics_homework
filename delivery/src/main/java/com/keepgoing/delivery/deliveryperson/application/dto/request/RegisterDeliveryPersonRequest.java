package com.keepgoing.delivery.deliveryperson.application.dto.request;

import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPersonType;

import java.util.UUID;

public record RegisterDeliveryPersonRequest(
        Long userId,
        String slackId,
        DeliveryPersonType type,
        UUID hubId  // VENDOR 타입일 때만 필수
) {
}