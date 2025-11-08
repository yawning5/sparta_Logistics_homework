package com.sparta.member.application.mapper;

import com.sparta.member.application.dto.SignUpRequestDto;
import com.sparta.member.domain.model.Member;
import com.sparta.member.domain.vo.Affiliation;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper implements ApplicationMapper {

    @Override
    public Member signUpRequestDtoToMember(SignUpRequestDto signUpRequestDto) {
        return Member.from(
            null,
            signUpRequestDto.name(),
            signUpRequestDto.password(),
            signUpRequestDto.email(),
            signUpRequestDto.slackId(),
            new Affiliation(
                signUpRequestDto.affiliation_Type(),
                signUpRequestDto.affiliation_Id(),
                signUpRequestDto.affiliation_name()
            ),
            signUpRequestDto.role()
        );
    }
}
