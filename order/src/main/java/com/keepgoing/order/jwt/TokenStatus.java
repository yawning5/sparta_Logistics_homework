package com.keepgoing.order.jwt;

public enum TokenStatus {
    VALID("유효"),
    EXPIRED("만료"),
    INVALID("무효")
    ;

    private final String description;

    TokenStatus(String description) {
        this.description = description;
    }

    public boolean isValid() {
        return this == VALID;
    }

    public boolean isExpired() {
        return this == EXPIRED;
    }

    public boolean isInvalid() {
        return this == INVALID;
    }
}
