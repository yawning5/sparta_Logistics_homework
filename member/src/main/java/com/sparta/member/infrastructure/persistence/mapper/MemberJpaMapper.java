package com.sparta.member.infrastructure.persistence.mapper;

import com.sparta.member.domain.model.Member;
import com.sparta.member.domain.vo.Affiliation;
import com.sparta.member.infrastructure.persistence.jpa.entity.MemberJpa;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MemberJpaMapper implements PersistenceMapper {

    @Override
    public Member toMember(MemberJpa memberJpa) {
        return Member.from(
            memberJpa.getId(),
            memberJpa.getName(),
            memberJpa.getPassword(),
            memberJpa.getEmail(),
            memberJpa.getSlackId(),
            new Affiliation(
                memberJpa.getAffiliationType(),
                memberJpa.getAffiliationId(),
                memberJpa.getAffiliationName()
            ),
            memberJpa.getRole()
        );
    }

    @Override
    public MemberJpa toMemberJpa(Member member) {
        return MemberJpa.builder()
            .name(member.accountInfo().name())
            .password(member.accountInfo().password())
            .slackId(member.accountInfo().slackId())
            .affiliationType(member.affiliation().type())
            .affiliationId(member.affiliation().id())
            .affiliationName(member.affiliation().name())
            .role(member.role())
            .status(member.status())
            .build();
    }

    @Override
    public List<Member> toMembers(List<MemberJpa> memberJpaList) {
        return memberJpaList.stream()
            .map(this::toMember)
            .toList()
            ;
    }

    @Override
    public List<MemberJpa> toMembersJpa(List<Member> members) {
        return members.stream()
            .map(this::toMemberJpa)
            .toList();
    }
}
