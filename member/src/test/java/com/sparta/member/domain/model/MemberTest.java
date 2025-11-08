package com.sparta.member.domain.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sparta.member.domain.enums.Role;
import com.sparta.member.domain.enums.Status;
import com.sparta.member.domain.vo.Affiliation;
import com.sparta.member.domain.vo.Type;
import com.sparta.member.fixture.MemberFixture;
import java.lang.reflect.Field;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

// RED - GREEN - Refactor 의 단계를 잘못 이해하고 있었다
public class MemberTest {

    @Nested
    @DisplayName("회원가입 요청 생성")
    class Member_signUp {

        private Object get(Object target, String field) throws Exception {
            Field f = target.getClass().getDeclaredField(field);
            f.setAccessible(true);
            return f.get(target);
        }
        
        @Nested
        @DisplayName("회원가입시 필수 사항 항목 검사")
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
            Affiliation affiliation = new Affiliation(
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

            assertAll(
                () -> assertNotNull(get(member, "affiliation"))
            );
        }

        @Test
        @DisplayName("회원 가입 요청 생성시 상태는 PENDING")
        void member_signUpRequest_StatusPending() {
            String username = "이름";
            String password = "비번";
            String email = "이메일";
            String slackId = "슬랙";
            Affiliation affiliation = new Affiliation(
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

            assertAll(
                () -> assertEquals(Status.PENDING, get(member, "status")),
                () -> assertEquals(username, get(member.accountInfo(), "name"))
            );
        }

        @Test
        @DisplayName("회원가입 요청 상태 APPROVE 변경")
        void member_signUpRequest_StatusApprove() {
            var m = MemberFixture.memberWithId(null, null);
            m.approve();
            assertEquals(Status.APPROVED, m.status());
        }

        @Test
        @DisplayName("회원가입 요청 상태 REJECTED 변경")
        void member_signUpRequest_StatusRejected() {
            var m =  MemberFixture.memberWithId(null, null);
            m.reject();
            assertEquals(Status.REJECTED, m.status());
        }
    }

}
