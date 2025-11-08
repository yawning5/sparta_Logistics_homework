package com.sparta.member.application.dto;

import com.sparta.member.domain.enums.Role;
import com.sparta.member.domain.vo.Type;
import java.util.UUID;

public record SignUpRequestDto(
    String name,
    String password,
    String email,
    String slackId,
    Type affiliation_Type,
    UUID affiliation_Id,
    String affiliation_name,
    Role role
) {

}
