package com.sparta.member.infrastructure.persistence.mapper;

import com.sparta.member.domain.model.Member;
import com.sparta.member.infrastructure.persistence.jpa.entity.MemberJpa;

public interface PersistenceMapper {
    Member toMember(MemberJpa memberJpa);

    MemberJpa toMemberJpa(Member member);
}
