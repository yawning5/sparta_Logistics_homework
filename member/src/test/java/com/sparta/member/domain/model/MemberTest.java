package com.sparta.member.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sparta.member.domain.enums.Role;
import com.sparta.member.domain.vo.Affiliation;
import com.sparta.member.domain.vo.Type;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

public class MemberTest {

    @Nested
    @DisplayName("회원가입 요청 생성")
    class Member_signUp {

        @Nested
        @DisplayName("RED")
        class Red {
            @Test
            @DisplayName("전달 인자중 null 이 포함")
            void member_signUpRequest() {
                // given
                String username = "hong";
                String password = "dfdf";
                String slackId = "fdfdf";
                String email = null;
                Affiliation affiliation = null;
                Role role = null;

                // when & then
                assertThrows(IllegalArgumentException.class, () -> {
                    Member.requestSignUp(
                        username,
                        password,
                        slackId,
                        email,
                        affiliation,
                        role
                    );
                });
            }
        }

        @Test
        @DisplayName("GREEN")
        void member_signUpRequest() {
            String username = "이름";
            String password = "비번";
            String email = "이메일";
            String slackId = "슬랙";
            Affiliation affiliation = Affiliation.of(
                Type.HUB,
                UUID.randomUUID(),
                "회사이름"
            );
            Role role = Role.MASTER;


            Member member
                = Member.requestSignUp(
                username,
                password,
                email,
                slackId,
                affiliation,
                role
            );

            assertEquals(email, member.getUsername());
            assertEquals(password, member.getPassword());
        }
    }

}
