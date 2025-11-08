package com.sparta.member.application.service;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sparta.member.application.mapper.ApplicationMapper;
import com.sparta.member.domain.model.Member;
import com.sparta.member.domain.repository.MemberRepository;
import com.sparta.member.fixture.MemberFixture;
import com.sparta.member.fixture.SignUpRequestDtoFixture;
import com.sparta.member.global.CustomException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class) // JUnit5 에서 Mockito 활성화
class MemberServiceTest {

    // 주입 받는 class
    @Mock
    MemberRepository memberRepository;
    @Mock
    ApplicationMapper mapper;
    @Mock
    PasswordEncoder passwordEncoder;


    // 실제 테스트 대상
    @InjectMocks
    MemberService memberService;

    @Nested
    @DisplayName("회원 가입 요청 생성 테스트")
    class requestSignUp {
        @Test
        @DisplayName("메서드 실행후 id 가 존재해야한다")
        void requestSignUpTest() {
            //given
            var s = SignUpRequestDtoFixture.normalRequest();
            var m = MemberFixture.memberWithId(SignUpRequestDtoFixture.NAME, SignUpRequestDtoFixture.PASSWORD);
            when(memberRepository.existsByEmail(anyString()))
                .thenReturn(Boolean.FALSE);
            when(memberRepository.save(any(Member.class)))
                .thenReturn(m);
            when(passwordEncoder.encode(any()))
                .thenReturn("123");


            //when
            Long id = memberService.requestSignUp(s);

            //then
            assertAll(
                () -> assertEquals(m.id(), id),
                () -> verify(memberRepository).save(any(Member.class))
            );
        }

        @Test
        @DisplayName("중복된 이메일로 가입 할 수 없음")
        void requestSignUpFailTest() {
            // given
            var s = SignUpRequestDtoFixture.normalRequest();
            var m = MemberFixture.memberWithId(SignUpRequestDtoFixture.NAME, SignUpRequestDtoFixture.PASSWORD);
            when( memberRepository.existsByEmail(anyString()))
                .thenReturn(Boolean.TRUE);
            // when
            var exception = assertThrows(CustomException.class, () -> {
                memberService.requestSignUp(s);
            });

            // then
            assertAll(
                () -> verify(memberRepository).existsByEmail(any(String.class)),
                () -> assertEquals("이미 존재하는 이메일입니다.", exception.getMessage())
            );
        }
    }

}