package com.keepgoing.delivery.delivery.domain.vo;

public record Duration(int minutes) {
    public Duration {
        if (minutes < 0) throw new IllegalArgumentException("소요 시간은 0분 이상입니다.");
    }
}
