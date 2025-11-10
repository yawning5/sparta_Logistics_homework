package com.keepgoing.member.interfaces.web;

<<<<<<< HEAD
import com.keepgoing.member.application.dto.BaseResponseDto;
import com.keepgoing.member.application.dto.LoginDto;
import com.keepgoing.member.application.service.AuthService;
import com.keepgoing.member.application.service.MemberService;
import com.keepgoing.member.interfaces.dto.SignUpRequestDto;
import com.keepgoing.member.interfaces.dto.StatusChangeRequestDto;
import com.keepgoing.member.interfaces.dto.StatusUpdateResponseDto;
=======
import com.sparta.member.application.service.AuthService;
import com.sparta.member.application.service.MemberService;
import com.sparta.member.infrastructure.userDetails.CustomUserDetails;
import com.sparta.member.interfaces.dto.BaseResponseDto;
import com.sparta.member.interfaces.dto.LoginDto;
import com.sparta.member.interfaces.dto.MemberInfoResponseDto;
import com.sparta.member.interfaces.dto.SearchRequestDto;
import com.sparta.member.interfaces.dto.SignUpRequestDto;
import com.sparta.member.interfaces.dto.StatusChangeRequestDto;
import com.sparta.member.interfaces.dto.StatusUpdateResponseDto;
>>>>>>> 84b265aace245f24c5ee8f8823dd3a33829a6688
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
    @PatchMapping("/{id}")
    public ResponseEntity<BaseResponseDto<?>> deleteMember(
        @PathVariable Long id,
        @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        memberService.deleteMember(id, userDetails.getUserId());
        return ResponseEntity.noContent()
            .build();
    }
}