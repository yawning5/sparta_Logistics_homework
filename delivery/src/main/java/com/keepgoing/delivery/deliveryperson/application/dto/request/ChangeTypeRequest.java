package com.keepgoing.delivery.deliveryperson.application.dto.request;

import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPersonType;

import java.util.UUID;

public record ChangeTypeRequest(
        DeliveryPersonType newType,
        UUID newHubId  // VENDOR로 전환 시 필수, HUB로 전환 시 무시
) {
}