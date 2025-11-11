package com.sparta.member;

import static io.restassured.RestAssured.given;
import static org.instancio.Select.field;

import com.sparta.member.interfaces.dto.request.LoginDto;
import com.sparta.member.domain.enums.Role;
import com.sparta.member.domain.enums.Status;
import com.sparta.member.domain.model.Member;
import com.sparta.member.domain.repository.MemberRepository;
import com.sparta.member.domain.vo.Affiliation;
import com.sparta.member.domain.vo.Type;
import com.sparta.member.interfaces.dto.request.SignUpRequestDto;
import com.sparta.member.interfaces.dto.request.StatusChangeRequestDto;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import java.util.UUID;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class E2E {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @LocalServerPort
    private int port;


    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        if (!memberRepository.existsByEmail("master@master.com")) {
            Member master = Member.requestSignUp(
                "마스터",
                passwordEncoder.encode("123"),
                "master@master.com",
                "master",
                new Affiliation(Type.COMPANY, UUID.randomUUID(), "sparta"),
                Role.MASTER
            );
            memberRepository.save(master);
        }
    }

    @Test
    @DisplayName("회원 생성 부터 로그인 까지")
    void createMemberReqAndFailLogin_E2E() {
        // given
        var memberReq = Instancio.of(SignUpRequestDto.class)
            .set(field(SignUpRequestDto::email), "test@test.com")
            .set(field(SignUpRequestDto::password), "123")
            .create();
        var loginDto = new LoginDto("test@test.com", "123");
        var loginMaster = new LoginDto("master@master.com", "123");
        var statusChangeReq = new StatusChangeRequestDto(
            "test@test.com",
            Status.APPROVED
        );

        String masterJwt =
            given().contentType(ContentType.JSON)
                .body(loginMaster)
                .when()
                .post("/v1/member/login")
                .then()
                .statusCode(200)
                .extract()
                .header("Authorization");

        // when: 회원 가입 요청 생성
        String location =
            given().contentType(ContentType.JSON)
                .body(memberReq)
                .when()
                .post("/v1/member/register")
                .then()
                .statusCode(201)
                .extract()
                .header("Location");

        Long newMemberId = Long.valueOf(location.substring(location.lastIndexOf('/') + 1));


        // then
        // PENDING 상태 로그인 실패
        given().contentType(ContentType.JSON)
            .body(loginDto)
            .when()
            .post("/v1/member/login")
            .then()
            .statusCode(401);


        // MASTER 권한 사용자가 APPROVED 상태로 변경
        given().contentType(ContentType.JSON)
            .header("Authorization", masterJwt)
            .body(statusChangeReq)
            .when()
            .patch("/v1/member/{id}/status", newMemberId)
            .then()
            .statusCode(204);

        // 이후 로그인 성공
        given().contentType(ContentType.JSON)
            .body(loginDto)
            .when()
            .post("/v1/member/login")
            .then()
            .statusCode(200);
    }
}
