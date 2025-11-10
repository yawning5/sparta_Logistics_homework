package com.keepgoing.member.interfaces.dto;

import com.keepgoing.member.domain.enums.Status;

public record StatusUpdateResponseDto(
    String email,
    String username,
    Status status
) {

}
