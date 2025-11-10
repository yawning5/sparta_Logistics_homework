package com.sparta.member.infrastructure.persistence.mapper;

import com.sparta.member.domain.model.Member;
import com.sparta.member.infrastructure.persistence.jpa.entity.MemberJpa;
import java.util.List;

public interface PersistenceMapper {
    Member toMember(MemberJpa memberJpa);

    MemberJpa toMemberJpa(Member member);

    List<Member> toMembers(List<MemberJpa> memberJpaList);

    List<MemberJpa> toMembersJpa(List<Member> members);
}
