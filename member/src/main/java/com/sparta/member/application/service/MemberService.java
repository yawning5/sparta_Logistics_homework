package com.sparta.member.application.service;

import com.sparta.member.application.dto.SignUpRequestDto;
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
        Member m = mapper.signUpRequestDtoToMember(requestDto);
        Member savedM = memberRepository.save(m);
        return savedM.id();
    }

    private void checkMember(SignUpRequestDto requestDto) {
        if (memberRepository.findByEmail(requestDto.email()) != null) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
    }

}
