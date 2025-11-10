package com.keepgoing.member.interfaces.dto;

public record SearchRequestDto(
    String name,
    String slackId,
    String affiliationType,
    String affiliationName,
    String email,
    String status
) {

}
