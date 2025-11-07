package com.sparta.member.interfaces.dto;

public record SearchRequestDto(
    String slackId,
    String affiliationType,
    String affiliationName,
    String email
) {

}
