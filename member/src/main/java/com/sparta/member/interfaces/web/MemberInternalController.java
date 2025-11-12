package com.sparta.member.interfaces.web;

import com.sparta.member.application.service.MemberService;
import com.sparta.member.infrastructure.userDetails.CustomUserDetails;
import com.sparta.member.interfaces.dto.BaseResponseDto;
import com.sparta.member.interfaces.dto.response.MemberInfoInternalResponseDto;
import com.sparta.member.interfaces.dto.response.MemberInfoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/internal/member")
@Slf4j
public class MemberInternalController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<BaseResponseDto<?>> getMember(
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {

        log.info("getMember");

        MemberInfoInternalResponseDto res = memberService.memberInfoForGateway(userDetails.getUserId());

        log.info(res.toString());

        return ResponseEntity.ok(
            BaseResponseDto.success(res)
        );
    }

}
