package com.keepgoing.order.infrastructure.persistence.repository.order;


import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OrderJpaRepositoryCustomImpl implements OrderJpaRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    private final EntityManager em;

}
