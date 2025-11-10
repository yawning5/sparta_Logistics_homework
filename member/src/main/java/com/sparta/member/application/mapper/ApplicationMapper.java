package com.sparta.member.application.mapper;

import com.sparta.member.domain.model.Member;
import com.sparta.member.interfaces.dto.MemberInfoResponseDto;
import com.sparta.member.interfaces.dto.StatusUpdateResponseDto;

public interface ApplicationMapper {

    StatusUpdateResponseDto toStatusUpdateResponseDto(Member savedMember);

    MemberInfoResponseDto toGetMemberResponseDto(Member byId);
}
