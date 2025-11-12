package com.keepgoing.order.jwt;

public record CustomPrincipal (
    String email,
    Long userId
) {

}
