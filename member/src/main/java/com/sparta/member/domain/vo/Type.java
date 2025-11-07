package com.sparta.member.domain.vo;

public enum Type {
    HUB("허브 소속"),
    COMPANY("회사 소속");

    Type(
        String description
    ) {
        this.description = description;
    }

    private final String description;

    public String getDescription() {
        return description;
    }
}
