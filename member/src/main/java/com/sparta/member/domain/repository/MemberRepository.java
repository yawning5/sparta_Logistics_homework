package com.sparta.member.domain.repository;

import com.sparta.member.domain.model.Member;
import com.sparta.member.infrastructure.persistence.jpa.entity.MemberJpa;
import java.util.Optional;

public interface MemberRepository {
    MemberJpa findByEmailUseInfra(String email);
    Member save(Member member);
    Member findByEmail(String email);
}
