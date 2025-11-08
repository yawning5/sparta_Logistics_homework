package com.sparta.member.infrastructure.config;

import static org.junit.jupiter.api.Assertions.*;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.proc.SecurityContext;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

class JwtConfigTest {

    private final String secret = "secretjshdfjahsdfkjasdhfjkadshfjksadhljfka";
    private JwtClaimsSet claims;

    @BeforeEach
    void setUp() {
        claims = JwtClaimsSet.builder()
            .subject("username")
            .issuedAt(Instant.now())
            .expiresAt(Instant.now().plusSeconds(10))
            .claim("role", "HUB")
            .claim("userId", "1")
            .build();

    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void encodeAndDecode_shouldWork() {
        // given
        SecretKey key = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        JwtEncoder encoder = new NimbusJwtEncoder(new ImmutableSecret<>(key));
        JwtDecoder decoder = NimbusJwtDecoder.withSecretKey(key).build();
        JwsHeader header = JwsHeader.with(() -> "HS256").build();

        //when
        String token = encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
        Jwt decoded = decoder.decode(token);

        assertAll(
            () -> assertEquals("username", decoded.getSubject()),
            () -> assertEquals("HUB", decoded.getClaims().get("role").toString()),
            () -> assertEquals("1", decoded.getClaims().get("userId").toString())
        );
    }

    @Test
    void beans_shouldBeCreatedSuccessfully() {
        // 스프링 컨텍스트 직접 생성
        try (var context = new AnnotationConfigApplicationContext(JwtConfig.class)) {

            // Bean 존재 여부 검증
            JwtEncoder encoder = context.getBean(JwtEncoder.class);
            JwtDecoder decoder = context.getBean(JwtDecoder.class);

            assertNotNull(encoder);
            assertNotNull(decoder);
        }
    }
}