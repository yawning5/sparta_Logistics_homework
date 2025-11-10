package com.sparta.member.application.mapper;

import com.sparta.member.domain.model.Member;
import com.sparta.member.interfaces.dto.MemberInfoResponseDto;
import com.sparta.member.interfaces.dto.StatusUpdateResponseDto;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper implements ApplicationMapper {

    @Override
    public StatusUpdateResponseDto toStatusUpdateResponseDto(Member savedMember) {
        return new StatusUpdateResponseDto(
            savedMember.accountInfo().email(),
            savedMember.accountInfo().name(),
            savedMember.status()
        );
    }

    @Override
    public MemberInfoResponseDto toGetMemberResponseDto(Member member) {
        return new MemberInfoResponseDto(
            member.accountInfo().name(),
            member.accountInfo().email(),
            member.accountInfo().slackId(),
            member.affiliation().type(),
            member.affiliation().name(),
            member.affiliation().id(),
            member.role(),
            member.status()
        );
    }
}
