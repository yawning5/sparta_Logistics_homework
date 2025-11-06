package com.sparta.member.infrastructure.persistence;

import com.sparta.member.domain.repository.MemberRepository;
import com.sparta.member.infrastructure.persistence.jpa.entity.MemberJpa;
import com.sparta.member.infrastructure.persistence.jpa.repo.SpringDataMemberJpaRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepository {

    private final SpringDataMemberJpaRepository springDataMemberJpaRepository;

    @Override
    public Optional<MemberJpa> findByEmail(String email) {
        return springDataMemberJpaRepository.findByEmail(email);
    }

    @Override
    public MemberJpa save(MemberJpa member) {
        return springDataMemberJpaRepository.save(member);
    }
}
