package com.sparta.member.application.service;

import com.sparta.member.domain.vo.Affiliation;
import com.sparta.member.interfaces.dto.SignUpRequestDto;
import com.sparta.member.application.mapper.ApplicationMapper;
import com.sparta.member.domain.model.Member;
import com.sparta.member.domain.repository.MemberRepository;
import com.sparta.member.global.CustomException;
import com.sparta.member.global.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ApplicationMapper mapper;

    public Long requestSignUp(SignUpRequestDto requestDto) {
        checkMember(requestDto);
        Member newMember = Member.requestSignUp(
            requestDto.name(),
            requestDto.password(),
            requestDto.email(),
            requestDto.slackId(),
            new Affiliation(
                requestDto.affiliation_Type(),
                requestDto.affiliation_Id(),
                requestDto.affiliation_name()
            ),
            requestDto.role()
        );
        Member savedMember = memberRepository.save(newMember);
        return savedMember.id();
    }

    private void checkMember(SignUpRequestDto requestDto) {
        if (memberRepository.findByEmail(requestDto.email()) != null) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

}
