package com.sparta.member.infrastructure.persistence.jpa.repo;

import com.sparta.member.domain.model.Member;
import com.sparta.member.infrastructure.persistence.jpa.entity.MemberJpa;
import com.sparta.member.infrastructure.persistence.jpa.querydsl.QueryDslMemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMemberJpaRepository extends
    JpaRepository<MemberJpa, Long>,
    QueryDslMemberRepository {

    Optional<MemberJpa> findByEmail(String email);
}
