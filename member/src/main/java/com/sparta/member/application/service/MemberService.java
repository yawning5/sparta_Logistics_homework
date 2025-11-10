package com.sparta.member.application.service;

import com.sparta.member.domain.enums.Status;
import com.sparta.member.domain.vo.Affiliation;
import com.sparta.member.interfaces.dto.MemberInfoResponseDto;
import com.sparta.member.interfaces.dto.SignUpRequestDto;
import com.sparta.member.application.mapper.ApplicationMapper;
import com.sparta.member.domain.model.Member;
import com.sparta.member.domain.repository.MemberRepository;
import com.sparta.member.global.CustomException;
import com.sparta.member.global.ErrorCode;
import com.sparta.member.interfaces.dto.StatusChangeRequestDto;
import com.sparta.member.interfaces.dto.StatusUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ApplicationMapper mapper;
    private final PasswordEncoder passwordEncoder;

    public Long requestSignUp(SignUpRequestDto requestDto) {
        if (memberRepository.existsByEmail(requestDto.email())) {
            throw new CustomException(ErrorCode.DUPLICATE_EMAIL);
        }
        Member newMember = Member.requestSignUp(
            requestDto.name(),
            passwordEncoder.encode(requestDto.password()),
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

    public StatusUpdateResponseDto updateStatus(StatusChangeRequestDto requestDto, Long id) {
        Member targetMember = memberRepository.findByEmail(requestDto.email());
        switch(requestDto.status()) {
            case APPROVED -> targetMember.approve();
            case REJECTED -> targetMember.reject();
        }
        Member savedMember = memberRepository.save(targetMember);
        return mapper.toStatusUpdateResponseDto(savedMember);
    }

    public MemberInfoResponseDto getMemberInfo(Long id) {
        return mapper.toGetMemberResponseDto(memberRepository.findById(id));
    }
}
