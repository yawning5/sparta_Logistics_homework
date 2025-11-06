package com.keepgoing.order.domain;

public enum OutBoxState {

    PENDING("생성됨"),
    PUBLISHING("처리중"),
    PUBLISHED("전송 완료"),
    RETRYING("전송 재시도"),
    FAILED("실패"),
    ;

    private String description;

    OutBoxState(String description) {
        this.description = description;
    }
}
