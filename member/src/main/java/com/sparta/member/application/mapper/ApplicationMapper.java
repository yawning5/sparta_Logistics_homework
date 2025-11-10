package com.keepgoing.member.application.mapper;

<<<<<<< HEAD
import com.keepgoing.member.domain.model.Member;
import com.keepgoing.member.interfaces.dto.StatusUpdateResponseDto;
=======
import com.sparta.member.domain.model.Member;
import com.sparta.member.interfaces.dto.MemberInfoResponseDto;
import com.sparta.member.interfaces.dto.StatusUpdateResponseDto;
>>>>>>> 84b265aace245f24c5ee8f8823dd3a33829a6688

public interface ApplicationMapper {

    StatusUpdateResponseDto toStatusUpdateResponseDto(Member savedMember);

    MemberInfoResponseDto toGetMemberResponseDto(Member byId);
}
