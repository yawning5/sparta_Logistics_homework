package com.sparta.member.infrastructure.persistence.jpa.querydsl;

import com.sparta.member.infrastructure.persistence.jpa.entity.MemberJpa;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QueryDslMemberRepository {
    Page<MemberJpa> findBySearchOption(
        Pageable pageable,
        String slackId,
        String affiliationType,
        String affiliationName,
        String email,
        String name,
        String status
    );

}
