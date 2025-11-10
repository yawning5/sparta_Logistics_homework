package com.keepgoing.delivery.delivery.domain.vo;

public record RouteSeq(int value) {
    public RouteSeq {
        if (value < 1) throw new IllegalArgumentException("경로 순서는 1부터 시작합니다.");
    }

    public RouteSeq next() {
        return new RouteSeq(this.value + 1);
    }
}
