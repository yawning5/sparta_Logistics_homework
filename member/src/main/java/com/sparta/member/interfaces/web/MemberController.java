package com.sparta.member.interfaces.web;

import com.sparta.member.application.dto.BaseResponseDto;
import com.sparta.member.application.dto.LoginDto;
import com.sparta.member.application.service.AuthService;
import com.sparta.member.application.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<?> login(
        @RequestBody LoginDto loginDto
    ) {
        String token = authService.login(loginDto);
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .body(BaseResponseDto.success(null));
    }

}
