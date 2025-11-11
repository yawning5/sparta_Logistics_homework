package com.sparta.member.interfaces.dto.request;

import com.sparta.member.domain.enums.Status;

public record StatusChangeRequestDto(
    String email,
    Status status
) {

}
