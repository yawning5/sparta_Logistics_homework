package com.sparta.member.infrastructure.persistence;

import com.sparta.member.domain.model.Member;
import com.sparta.member.domain.repository.MemberRepository;
import com.sparta.member.global.CustomException;
import com.sparta.member.global.ErrorCode;
import com.sparta.member.infrastructure.persistence.jpa.entity.MemberJpa;
import com.sparta.member.infrastructure.persistence.jpa.repo.SpringDataMemberJpaRepository;
import com.sparta.member.infrastructure.persistence.mapper.MemberJpaMapper;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MemberRepositoryImpl implements MemberRepository {

    private final SpringDataMemberJpaRepository springDataMemberJpaRepository;
    private final MemberJpaMapper mapper;

    @Override
    public MemberJpa findByEmailUseInfra(String email) {
        return springDataMemberJpaRepository.findByEmail(email)
            .orElseThrow(() ->
                new CustomException(ErrorCode.MEMBER_NOT_FOUND)
            );
    }

    @Override
    public Member save(Member member) {
        return mapper.toMember(springDataMemberJpaRepository.save(mapper.toMemberJpa(member)));
    }

    @Override
    public Member findByEmail(String email) {
        return mapper.toMember(
            springDataMemberJpaRepository.findByEmail(email)
                .orElseThrow(() ->
                    new CustomException(ErrorCode.MEMBER_NOT_FOUND)
                )
        );
    }
}
