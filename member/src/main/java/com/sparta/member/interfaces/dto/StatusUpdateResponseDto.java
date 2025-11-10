package com.sparta.member.interfaces.dto;

import com.sparta.member.domain.enums.Status;

public record StatusUpdateResponseDto(
    String email,
    String username,
    Status status
) {

}
