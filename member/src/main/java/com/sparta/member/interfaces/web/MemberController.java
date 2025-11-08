package com.sparta.member.interfaces.web;

import com.sparta.member.application.dto.BaseResponseDto;
import com.sparta.member.application.dto.LoginDto;
import com.sparta.member.application.service.AuthService;
import com.sparta.member.application.service.MemberService;
import com.sparta.member.interfaces.dto.RegisterRequestDto;
import com.sparta.member.interfaces.dto.StatusChangeRequestDto;
import jakarta.ws.rs.PATCH;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping("/register")
    public ResponseEntity<?> register(
        @RequestBody RegisterRequestDto requestDto
    ) {

        return null;
    }

    @PreAuthorize("hasRole('MASTER')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(
        @PathVariable Long id,
        @RequestBody StatusChangeRequestDto requestDto
    ) {

        return null;
    }

}