package com.sparta.member.interfaces.dto;

import com.sparta.member.domain.enums.Role;

public record RoleChangeRequestDto(
    Role role
) {

}
