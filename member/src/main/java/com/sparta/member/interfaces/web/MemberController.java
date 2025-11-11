package com.sparta.member.interfaces.web;

import com.sparta.member.application.service.AuthService;
import com.sparta.member.application.service.MemberService;
import com.sparta.member.infrastructure.userDetails.CustomUserDetails;
import com.sparta.member.interfaces.dto.BaseResponseDto;
import com.sparta.member.interfaces.dto.RoleChangeRequestDto;
import com.sparta.member.interfaces.dto.request.ChangeInfoRequestDto;
import com.sparta.member.interfaces.dto.request.LoginDto;
import com.sparta.member.interfaces.dto.response.MemberInfoResponseDto;
import com.sparta.member.interfaces.dto.request.SearchRequestDto;
import com.sparta.member.interfaces.dto.request.SignUpRequestDto;
import com.sparta.member.interfaces.dto.request.StatusChangeRequestDto;
import com.sparta.member.interfaces.dto.response.StatusUpdateResponseDto;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
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

    // TODO: 공부 필요
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDto<MemberInfoResponseDto>> getMemberById(
        @PathVariable Long id
    ) {
        MemberInfoResponseDto res = memberService.getMemberInfo(id);

        return ResponseEntity.ok()
            .body(BaseResponseDto.success(res));
    }

    @PostMapping("/login")
    public ResponseEntity<BaseResponseDto<?>> login(
        @RequestBody LoginDto loginDto
    ) {
        String token = authService.login(loginDto);
        return ResponseEntity.ok()
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .body(BaseResponseDto.success(null));
    }

    @PostMapping("/register")
    public ResponseEntity<BaseResponseDto<?>> register(
        @RequestBody @Validated SignUpRequestDto requestDto
    ) {
        Long id = memberService.requestSignUp(requestDto);

        URI uri = URI.create("/v1/member/" + id);

        return ResponseEntity.created(uri)
            .body(BaseResponseDto.success(null));
    }

    @PreAuthorize("hasRole('MASTER')")
    @PatchMapping("/{id}/status")
    public ResponseEntity<BaseResponseDto<?>> updateStatus(
        @PathVariable Long id,
        @RequestBody StatusChangeRequestDto requestDto
    ) {

        StatusUpdateResponseDto res = memberService.updateStatus(requestDto, id);

        return ResponseEntity.noContent()
            .build();
    }

    @PreAuthorize("hasRole('MASTER')")
    @GetMapping("/search")
    public ResponseEntity<BaseResponseDto<Page<MemberInfoResponseDto>>> search(
        SearchRequestDto searchRequestDto,
        Pageable pageable
    ) {
        Page<MemberInfoResponseDto> res = memberService.searchMembers(searchRequestDto, pageable);

        return ResponseEntity.ok(BaseResponseDto.success(res));
    }

    @PreAuthorize("hasRole('MASTER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseDto<?>> deleteMember(
        @PathVariable Long id,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        memberService.deleteMember(id, userDetails.getUserId());
        return ResponseEntity.noContent()
            .build();
    }

    @PreAuthorize("hasRole('MASTER')")
    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponseDto<?>> changeInfo(
        @PathVariable Long id,
        @RequestBody ChangeInfoRequestDto requestDto
    ) {
        memberService.changeInfo(id, requestDto);

        return ResponseEntity.noContent()
            .build();
    }

    @PreAuthorize("hasRole('MASTER')")
    @PatchMapping("/{id}/role")
    public ResponseEntity<BaseResponseDto<?>> changeRole(
        @PathVariable Long id,
        @RequestBody RoleChangeRequestDto reqDto
    ) {
        memberService.changeRole(id, reqDto);

        return ResponseEntity.noContent()
            .build();
    }
}