package com.sparta.member.application.service;

import com.sparta.member.application.dto.SignUpRequestDto;
import com.sparta.member.application.mapper.ApplicationMapper;
import com.sparta.member.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ApplicationMapper mapper;

    public Long requestSignUp(SignUpRequestDto requestDto) {
        return null;
    }

}
