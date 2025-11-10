package com.keepgoing.member.interfaces.dto;

import com.keepgoing.member.domain.enums.Status;

public record StatusChangeRequestDto(
    String email,
    Status status
) {

}
