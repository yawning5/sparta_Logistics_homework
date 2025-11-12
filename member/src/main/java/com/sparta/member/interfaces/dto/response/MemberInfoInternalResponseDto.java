package com.sparta.member.interfaces.dto.response;

import com.sparta.member.domain.enums.Role;
import com.sparta.member.domain.enums.Status;

public record MemberInfoInternalResponseDto(
    Long userId,
    Role role,
    Status status
) {

}
