package com.sparta.member.application.service;

import com.sparta.member.domain.enums.Role;
import com.sparta.member.domain.vo.Affiliation;
import com.sparta.member.domain.vo.Type;
import com.sparta.member.interfaces.dto.request.RoleChangeRequestDto;
import com.sparta.member.interfaces.dto.request.ChangeInfoRequestDto;
import com.sparta.member.interfaces.dto.response.MemberInfoInternalResponseDto;
import com.sparta.member.interfaces.dto.response.MemberInfoResponseDto;
import com.sparta.member.interfaces.dto.request.SearchRequestDto;
import com.sparta.member.interfaces.dto.request.SignUpRequestDto;
import com.sparta.member.application.mapper.ApplicationMapper;
import com.sparta.member.domain.model.Member;
import com.sparta.member.domain.repository.MemberRepository;
import com.sparta.member.global.CustomException;
import com.sparta.member.global.ErrorCode;
import com.sparta.member.interfaces.dto.request.StatusChangeRequestDto;
import com.sparta.member.interfaces.dto.response.StatusUpdateResponseDto;
import com.sparta.member.interfaces.feign.DeliveryClient;
import com.sparta.member.interfaces.feign.dto.request.DeliveryManRequestDto;
import com.sparta.member.interfaces.feign.enums.DeliveryType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final ApplicationMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final DeliveryClient deliveryClient;
    List<Integer> pagePolicy = new ArrayList<>(Arrays.asList(10, 30, 50));

    @Transactional
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

    @Transactional
    public StatusUpdateResponseDto updateStatus(StatusChangeRequestDto requestDto, Long id) {
        Member targetMember = memberRepository.findByEmail(requestDto.email());
        if (targetMember.role() == Role.DELIVERY && targetMember.affiliation().isType(Type.COMPANY)) {
            targetMember = spartaDeliveryMan(targetMember);
        }
        switch (requestDto.status()) {
            case APPROVED -> {
                targetMember.approve();
                if (targetMember.role() == Role.DELIVERY) {
                    createDeliveryMan(
                        targetMember.id(),
                        targetMember.accountInfo().slackId(),
                        targetMember.affiliation().type(),
                        targetMember.affiliation().id()
                    );
                }
            }
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

    @Transactional
    public void deleteMember(Long targetId, Long masterId) {
        Member member = memberRepository.findById(targetId);
        member.delete(masterId);
        memberRepository.save(member);
    }

    @Transactional
    public void changeInfo(Long id, ChangeInfoRequestDto requestDto) {
        Member targetMember = memberRepository.findById(id);
        Member changeMember = targetMember.changeInfo(
            requestDto.name(),
            passwordEncoder.encode(requestDto.password()),
            requestDto.email(),
            requestDto.slackId(),
            requestDto.affiliationType(),
            requestDto.affiliationId(),
            requestDto.affiliationName(),
            requestDto.role(),
            requestDto.status()
        );


        memberRepository.save(changeMember);
    }

    @Transactional
    public void changeRole(Long id, RoleChangeRequestDto reqDto) {
        Member targetMember = memberRepository.findById(id);
        targetMember.changRole(reqDto.role());
        System.out.println(targetMember.role().toString());
        memberRepository.save(targetMember);
    }

    public MemberInfoInternalResponseDto memberInfoForGateway(Long userId) {
        return mapper.toMemberInfoInternalDto(memberRepository.findById(userId));
    }

    private void createDeliveryMan(Long id, String slackId, Type type, UUID affiliationId) {
        deliveryClient.register(new DeliveryManRequestDto(
            id,
            slackId,
            DeliveryType.fromValue(type),
            affiliationId
        ));
    }

    private Member spartaDeliveryMan(Member member) {
        member.changeInfo(
            null,
            null,
            null,
            null,
            null,
            UUID.fromString("11111111-2222-3333-4444-555555555555"),
            null,
            null,
            null
        );

        return member;
    }
}
