package com.sparta.member.interfaces.dto.request;

import com.sparta.member.domain.enums.Role;
import com.sparta.member.domain.enums.Status;
import com.sparta.member.domain.vo.Type;
import java.util.UUID;

public record ChangeInfoRequestDto(
    String name,
    String password,
    String email,
    String slackId,
    Type affiliationType,
    UUID affiliationId,
    String affiliationName,
    Role role,
    Status status
) {

}
