package com.sparta.member.infrastructure.persistence.jpa.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.member.domain.vo.Type;
import com.sparta.member.infrastructure.persistence.jpa.entity.MemberJpa;
import com.sparta.member.infrastructure.persistence.jpa.entity.QMemberJpa;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
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

        BooleanBuilder where = new BooleanBuilder();
        if (slackId != null) {
            where.and(qMemberJpa.slackId.eq(slackId));
        }
        if (affiliationType != null) {
            where.and(qMemberJpa.affiliationType.eq(Type.valueOf(affiliationType)));
        }
        if (affiliationName != null) {
            where.and(qMemberJpa.affiliationName.eq(affiliationName));
        }
        if (email != null) {
            where.and(qMemberJpa.email.eq(email));
        }

        List<MemberJpa> members = query
            .selectFrom(qMemberJpa)
            .where(where)
            .orderBy(getOrderSpecifiers(pageable, qMemberJpa))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        JPAQuery<Long> count = query
            .select(qMemberJpa.id.count())
            .from(qMemberJpa)
            .where(where);

        return PageableExecutionUtils.getPage(members, pageable, count::fetchOne);
    }

    // Spring Pageable 의 Sort 정보를 QueryDSL 이 이해할 수 있는 형태로 바꾸는 코드
    // Pageable 안에는 Sort 정보가 있음
    // QueryDSL 은 orderBy() 에 OrderSpecifier<?> 타입을 요구
    // 그래서 Spring sort -> QueryDSL OrderSpecifier 로 변환해주는 중간 단계가 필요
    private OrderSpecifier<?>[] getOrderSpecifiers(Pageable pageable, QMemberJpa qMemberJpa) {
        List<OrderSpecifier<?>> orderSpecifiers = new ArrayList<>();

        // Pageable 에서 정렬조건이 있으면 변환
        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<?> pathBuilder
                = new PathBuilder<>(qMemberJpa.getType(), qMemberJpa.getMetadata());
            orderSpecifiers.add(
                new OrderSpecifier<>(
                    order.isAscending() ? Order.ASC : Order.DESC,
                    pathBuilder.getComparable(order.getProperty(), Comparable.class)
                )
            );
        }

        // 정렬 조건이 없으면 기본값
        if (orderSpecifiers.isEmpty()) {
            orderSpecifiers.add(qMemberJpa.createdAt.desc());
            orderSpecifiers.add(qMemberJpa.updatedAt.desc());
        }

        return orderSpecifiers.toArray(new OrderSpecifier[0]);
    }

}
