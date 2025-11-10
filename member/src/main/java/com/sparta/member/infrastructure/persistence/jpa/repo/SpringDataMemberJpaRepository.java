package com.keepgoing.member.infrastructure.persistence.jpa.repo;

import com.keepgoing.member.domain.model.Member;
import com.keepgoing.member.infrastructure.persistence.jpa.entity.MemberJpa;
import com.keepgoing.member.infrastructure.persistence.jpa.querydsl.QueryDslMemberRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataMemberJpaRepository extends
    JpaRepository<MemberJpa, Long>,
    QueryDslMemberRepository {

    Optional<MemberJpa> findByEmail(String email);
}
