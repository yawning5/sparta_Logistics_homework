package com.sparta.member.interfaces.dto;

import com.sparta.member.domain.enums.Role;
import com.sparta.member.domain.enums.Status;
import com.sparta.member.domain.vo.Type;
import java.time.LocalDateTime;
import java.util.UUID;

public record MemberInfoResponseDto(
    String name,
    String email,
    String slackId,
    Type affiliationType,
    String affiliationName,
    UUID affiliationId,
    Role Role,
    Status status,
    LocalDateTime deletedAt,
    Long deletedBy
) {
}
