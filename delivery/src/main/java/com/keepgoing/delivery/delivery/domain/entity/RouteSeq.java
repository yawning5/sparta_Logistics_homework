package com.keepgoing.delivery.delivery.domain.entity;

public record RouteSeq(int value) {
    public RouteSeq {
        if (value < 1) throw new IllegalArgumentException("경로 순서는 1부터 시작합니다.");
    }
}
