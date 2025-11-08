package com.sparta.member.infrastructure.persistence.jpa;

import com.sparta.member.domain.model.Member;
import com.sparta.member.domain.repository.MemberRepository;
import com.sparta.member.global.CustomException;
import com.sparta.member.global.ErrorCode;
import com.sparta.member.infrastructure.persistence.jpa.entity.MemberJpa;
import com.sparta.member.infrastructure.persistence.jpa.repo.SpringDataMemberJpaRepository;
import com.sparta.member.infrastructure.persistence.mapper.MemberJpaMapper;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class MemberRepositoryImpl implements MemberRepository {

    private final SpringDataMemberJpaRepository memberJpaRepository;
    private final MemberJpaMapper mapper;

    @Override
    public MemberJpa findByEmailUseInfra(String email) {
        return memberJpaRepository.findByEmail(email)
            .orElseThrow(() ->
                new CustomException(ErrorCode.MEMBER_NOT_FOUND)
            );
    }

    @Override
    public Member save(Member member) {
        return mapper.toMember(memberJpaRepository.save(mapper.toMemberJpa(member)));
    }

    @Override
    public Member findByEmail(String email) {
        return mapper.toMember(
            memberJpaRepository.findByEmail(email)
                .orElseThrow(() ->
                    new CustomException(ErrorCode.MEMBER_NOT_FOUND)
                )
        );
    }

    @Override
    public Page<Member> findBySearchOption(
        Pageable pageable,
        String slackId,
        String affiliationType,
        String affiliationName,
        String email) {

        Page<MemberJpa> MembersJpa = memberJpaRepository.findBySearchOption(pageable, slackId, affiliationType, affiliationName, email);

        return MembersJpa.map(mapper::toMember);
    }

    @Override
    public List<Member> saveAll(List<Member> members) {
        return mapper.toMembers(memberJpaRepository.saveAll(mapper.toMembersJpa(members)));
    }

}
