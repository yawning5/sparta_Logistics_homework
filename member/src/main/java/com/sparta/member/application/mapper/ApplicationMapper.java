package com.sparta.member.application.mapper;

import com.sparta.member.domain.model.Member;
import com.sparta.member.interfaces.dto.response.MemberInfoResponseDto;
import com.sparta.member.interfaces.dto.response.StatusUpdateResponseDto;

public interface ApplicationMapper {

    StatusUpdateResponseDto toStatusUpdateResponseDto(Member savedMember);

    MemberInfoResponseDto toGetMemberResponseDto(Member byId);
}
