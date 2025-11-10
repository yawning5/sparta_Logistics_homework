package com.keepgoing.member.infrastructure.persistence.mapper;

import com.keepgoing.member.domain.model.Member;
import com.keepgoing.member.infrastructure.persistence.jpa.entity.MemberJpa;
import java.util.List;

public interface PersistenceMapper {
    Member toMember(MemberJpa memberJpa);

    MemberJpa toMemberJpa(Member member);

    List<Member> toMembers(List<MemberJpa> memberJpaList);

    List<MemberJpa> toMembersJpa(List<Member> members);
}
