package com.sparta.member.application.mapper;

import com.sparta.member.domain.model.Member;
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
}
