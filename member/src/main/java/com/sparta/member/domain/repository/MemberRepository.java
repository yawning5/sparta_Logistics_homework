package com.keepgoing.member.domain.repository;

import com.keepgoing.member.domain.model.Member;
import com.keepgoing.member.infrastructure.persistence.jpa.entity.MemberJpa;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberRepository {

    MemberJpa findByEmailUseInfra(String email);

    Member save(Member member);

    Member findByEmail(String email);

    Page<Member> findBySearchOption(
        Pageable pageable,
        String slackId,
        String affiliationType,
        String affiliationName,
        String email,
        String name,
        String status
    );

    List<Member> saveAll(List<Member> members);

    boolean existsByEmail(String email);

    Member findById(Long id);
}
