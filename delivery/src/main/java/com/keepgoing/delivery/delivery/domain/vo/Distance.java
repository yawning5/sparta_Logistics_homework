package com.keepgoing.delivery.delivery.domain.vo;

public record Distance(double km) {
    public Distance {
        if (km < 0) throw new IllegalArgumentException("거리는 0 이상입니다.");
    }
}
