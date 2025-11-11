package com.sparta.member.interfaces.dto.request;

public record SearchRequestDto(
    String name,
    String slackId,
    String affiliationType,
    String affiliationName,
    String email,
    String status
) {

}
