package com.sparta.member.fixture;

import com.sparta.member.domain.enums.Role;
import com.sparta.member.domain.vo.Type;
import com.sparta.member.interfaces.dto.request.SignUpRequestDto;
import java.util.UUID;

public final class SignUpRequestDtoFixture {

    private SignUpRequestDtoFixture() {}

    // === 기본 테스트 값 ===
    public static final String NAME = "홍길동";
    public static final String PASSWORD = "1234";
    public static final String EMAIL = "hong@test.com";
    public static final String SLACK_ID = "hong_slack";
    public static final Type AFFILIATION_TYPE = Type.HUB;
    public static final UUID AFFILIATION_ID = UUID.randomUUID();
    public static final String AFFILIATION_NAME = "스파르타";
    public static final Role ROLE = Role.MASTER;

    // === 기본 DTO 생성 ===
    public static SignUpRequestDto normalRequest() {
        return new SignUpRequestDto(
            NAME,
            PASSWORD,
            EMAIL,
            SLACK_ID,
            AFFILIATION_TYPE,
            AFFILIATION_ID,
            AFFILIATION_NAME,
            ROLE
        );
    }

    // === 커스텀 생성 ===
    public static SignUpRequestDto of(
        String name,
        String password,
        String email,
        String slackId,
        Type affiliationType,
        UUID affiliationId,
        String affiliationName,
        Role role
    ) {
        return new SignUpRequestDto(
            name,
            password,
            email,
            slackId,
            affiliationType,
            affiliationId,
            affiliationName,
            role
        );
    }
}