package com.keepgoing.member.application.mapper;

<<<<<<< HEAD
import com.keepgoing.member.domain.model.Member;
import com.keepgoing.member.interfaces.dto.StatusUpdateResponseDto;
=======
import com.sparta.member.domain.model.Member;
import com.sparta.member.interfaces.dto.MemberInfoResponseDto;
import com.sparta.member.interfaces.dto.StatusUpdateResponseDto;
>>>>>>> 84b265aace245f24c5ee8f8823dd3a33829a6688
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
            member.status(),
            member.deletedAt(),
            member.deleteBy()
        );
    }
}
