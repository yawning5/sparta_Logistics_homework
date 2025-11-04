package com.sparta.member.doamin.enums;

public enum Status {
    PENDING("승인 대기"),
    APPROVED("회원가입 요청 수락됨"),
    REJECTED("회원가입 요청 거부됨"),
    DELETED("회원 탈퇴");

    Status(
        String description
    ) {
        this.description = description;
    }

    private final String description;

    public String getDescription() {
        return description;
    }
}
