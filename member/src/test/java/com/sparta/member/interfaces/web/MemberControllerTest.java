package com.sparta.member.interfaces.web;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.member.application.service.AuthService;
import com.sparta.member.application.service.MemberService;
import com.sparta.member.config.TestSecurityConfig;
import com.sparta.member.interfaces.dto.SignUpRequestDto;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MemberController.class)
@ActiveProfiles("test")
@Import(TestSecurityConfig.class)
class MemberControllerTest {

    // DispatcherServlet 을 실제로 동작시키지만
    // 톰캣 서버(포트)는 띄우지 않고 메모리 내에서 요청/응답을 시뮬레이션함.
    // 내부적으로 MockHttpServletRequest / MockHttpServletResponse 사용.
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MemberService memberService;
    @MockitoBean
    private AuthService authService;
    @Autowired
    private ObjectMapper objectMapper;

    @Nested
    @DisplayName("회원 가입 요청")
    class RegisterMemberTest {

        @Test
        @DisplayName("요청 값에 null 이 있다면 예외발생")
        void NullArgsException() throws Exception {
            // given
            var reqDto = Instancio.of(SignUpRequestDto.class)
                .set(Select.field(SignUpRequestDto.class, "email"), null)
                .create();

            // when, then
            mockMvc.perform(post("/v1/member/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("응답 헤더에 리소스URI 검증")
        void SuccessURIHeader() throws Exception {
            // given
            var reqDto = Instancio.of(SignUpRequestDto.class)
                .create();
            when(memberService.requestSignUp(any())).thenReturn(1L);

            // when, then
            mockMvc.perform(post("/v1/member/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqDto)))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(header().string("Location", "/v1/member/1"));
        }
    }
}