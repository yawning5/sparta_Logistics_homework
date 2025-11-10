package com.keepgoing.member.infrastructure.persistence.mapper;

import com.keepgoing.member.domain.model.Member;
import com.keepgoing.member.domain.vo.Affiliation;
import com.keepgoing.member.infrastructure.persistence.jpa.entity.MemberJpa;
import java.util.List;
import org.springframework.stereotype.Component;


/*
 TODO: 매퍼적용할것(MapStruct, model Mapper) 리플렉션 기반 직접구현보단 Mapper 가 낫다
 테스트 복잡도 또한 매퍼가 본인 단일 매핑만 한다고 가정하면 복잡성이 올라가지는 않는다
 */
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
            memberJpa.getRole(),
            memberJpa.getStatus(),
            memberJpa.getCreatedAt(),
            memberJpa.getUpdatedAt(),
            memberJpa.getDeletedAt(),
            memberJpa.getDeleteBy()
        );
    }

    @Override
    public MemberJpa toMemberJpa(Member member) {
        return MemberJpa.builder()
            .id(member.id())
            .name(member.accountInfo().name())
            .password(member.accountInfo().password())
            .email(member.accountInfo().email())
            .slackId(member.accountInfo().slackId())
            .affiliationType(member.affiliation().type())
            .affiliationId(member.affiliation().id())
            .affiliationName(member.affiliation().name())
            .role(member.role())
            .status(member.status())
            .createdAt(member.createdAt())
            .updatedAt(member.updatedAt())
            .deletedAt(member.deletedAt())
            .deleteBy(member.deleteBy())
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
