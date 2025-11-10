package com.sparta.vendor.domain.vo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    MASTER("마스터 관리자"),
    HUB("허브 관리자"),
    DELIVERY("배송 담당자"),
    COMPANY("업체 담당자");

    private final String description;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}
