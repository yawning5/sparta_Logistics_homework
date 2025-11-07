package com.sparta.member.infrastructure.persistence.jpa.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.member.infrastructure.persistence.jpa.entity.MemberJpa;
import com.sparta.member.infrastructure.persistence.jpa.entity.QMemberJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QueryDslMemberRepositoryImpl implements QueryDslMemberRepository{

    private final JPAQueryFactory query;

    @Override
    public Page<MemberJpa> findBySearchOption(
        Pageable pageable,
        String slackId,
        String affiliationType,
        String affiliationName,
        String email
    ) {

        QMemberJpa qMemberJpa = QMemberJpa.memberJpa;




        return null;
    }


}
