package com.keepgoing.order.domain.vo;

import java.util.Objects;

public record Member(
    Long memberId
){
    public Member {
        if (memberId == null) throw new IllegalArgumentException("주문자 아이디는 필수입니다.");
        if (memberId < 0L) throw new IllegalArgumentException("주문자 아이디는 0L보다 커야합니다.");
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Member member = (Member) o;
        return Objects.equals(memberId, member.memberId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(memberId);
    }
}
