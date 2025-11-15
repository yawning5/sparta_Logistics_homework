package com.keepgoing.order.infrastructure.persistence.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Column(name="member_id", nullable = false)
    private Long memberId;
}
