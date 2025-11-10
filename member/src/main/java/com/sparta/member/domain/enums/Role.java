package com.keepgoing.member.domain.enums;

public enum Role {
    MASTER("마스터 관리자"),
    HUB("허브 관리자"),
    DELIVERY("배송 담당자"),
    COMPANY("거래처 사용자");

    Role(
        String description
    ) {
        this.description = description;
    }

    private final String description;

    public String getDescription() {
        return description;
    }
}
