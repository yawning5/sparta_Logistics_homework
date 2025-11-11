package com.keepgoing.gateway.dto;

public record MemberResponseDto(
    Long userId,
    String role,
    String status
) {

}
