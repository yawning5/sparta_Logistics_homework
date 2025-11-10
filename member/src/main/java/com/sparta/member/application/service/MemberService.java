package com.sparta.member.application.service;

import com.sparta.member.domain.enums.Status;
import com.sparta.member.domain.vo.Affiliation;
import com.sparta.member.interfaces.dto.MemberInfoResponseDto;
import com.sparta.member.interfaces.dto.SearchRequestDto;
import com.sparta.member.interfaces.dto.SignUpRequestDto;
import com.sparta.member.application.mapper.ApplicationMapper;
import com.sparta.member.domain.model.Member;
import com.sparta.member.domain.repository.MemberRepository;
import com.sparta.member.global.CustomException;
import com.sparta.member.global.ErrorCode;
import com.sparta.member.interfaces.dto.StatusChangeRequestDto;
import com.sparta.member.interfaces.dto.StatusUpdateResponseDto;
import jakarta.persistence.criteria.CriteriaBuilder.In;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final ApplicationMapper mapper;
    private final PasswordEncoder passwordEncoder;
    List<Integer> pagePolicy = new ArrayList<>(Arrays.asList(10, 30, 50));

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
        switch (requestDto.status()) {
            case APPROVED -> targetMember.approve();
            case REJECTED -> targetMember.reject();
        }
        Member savedMember = memberRepository.save(targetMember);
        return mapper.toStatusUpdateResponseDto(savedMember);
    }

    public MemberInfoResponseDto getMemberInfo(Long id) {
        return mapper.toGetMemberResponseDto(memberRepository.findById(id));
    }

    public Page<MemberInfoResponseDto> searchMembers(SearchRequestDto searchRequestDto,
        Pageable pageable) {
        Pageable correctPageable = pageable;
        if (!pagePolicy.contains(pageable.getPageSize())) {
            correctPageable
                = PageRequest.of(pageable.getPageNumber(), 10, pageable.getSort());
        }

        Page<Member> searchedMember = memberRepository.findBySearchOption(
            correctPageable,
            searchRequestDto.slackId(),
            searchRequestDto.affiliationType(),
            searchRequestDto.affiliationName(),
            searchRequestDto.email(),
            searchRequestDto.name(),
            searchRequestDto.status()
        );

        return searchedMember.map(mapper::toGetMemberResponseDto);
    }
}
