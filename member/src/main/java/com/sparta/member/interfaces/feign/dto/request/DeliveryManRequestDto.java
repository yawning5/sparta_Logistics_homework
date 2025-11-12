package com.sparta.member.interfaces.feign.dto.request;

import com.sparta.member.interfaces.feign.enums.DeliveryType;
import java.util.UUID;

public record DeliveryManRequestDto(
    Long userId,
    String slackId,
    DeliveryType type,
    UUID hubId  // VENDOR 타입일 때만 필수
) {
}
