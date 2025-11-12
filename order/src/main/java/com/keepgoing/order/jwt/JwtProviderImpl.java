package com.keepgoing.order.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

@Slf4j(topic = "JwtProviderImpl")
@Component
public class JwtProviderImpl implements JwtProvider{

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.validity}")
    private long tokenValidity;

    @Override
    public TokenStatus validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token);
            return TokenStatus.VALID;
        } catch (ExpiredJwtException e) {
            return TokenStatus.EXPIRED;
        } catch (JwtException | IllegalArgumentException e) {
            return TokenStatus.INVALID;
        }
    }

    @Override
    public String getEmail(String token) {
        return getClaims(token)
            .getSubject();
    }

    @Override
    public List<String> getRoles(String token) {
        List<?> temp = getClaims(token)
            .get("roles", List.class);
        return temp.stream()
            .map(Object::toString)
            .toList();
    }

    @Override
    public String getUserId(String token) {
        return getClaims(token)
            .get("userId", String.class);
    }

    @Override
    public String getAffiliationId(String token) {
        return getClaims(token)
            .get("affiliationId", String.class);
    }

//    @Override
//    public Authentication getAuthentication(String token) {
//        Claims claims = getClaims(token);
//        String email = claims.getSubject();
//
//        List<String> roles = ((List<?>) claims.get("roles", List.class)).stream()
//            .map(Object::toString)
//            .toList();
//
//        List<? extends GrantedAuthority> authorities = roles.stream()
//            .map(SimpleGrantedAuthority::new)
//            .toList();
//
//        return new UsernamePasswordAuthenticationToken(email, null, authorities);
//    }

    @Override
    public Authentication getAuthentication(String token) {
        Claims claims = getClaims(token);

        String email = claims.getSubject();
        Long userId = Long.valueOf(claims.get("userId", String.class));


        CustomPrincipal principal = new CustomPrincipal(email, userId);

        String role = claims.get("role", String.class);
        List<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority(role)
        );

        return new UsernamePasswordAuthenticationToken(principal, null, authorities);
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        } catch (ExpiredJwtException e) {
            log.info("AccessToken 만료");
            return e.getClaims();
        } catch (JwtException | IllegalArgumentException e) {
            throw new IllegalStateException("변형된 토큰입니다.");
        }
    }

    private Key getKey() {
        // 키를 String 타입으로 넘길 수 없어 Key 객체로 만들어 전달
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public String createAccessToken(String email, String role, String userId, String affiliationId) {
        Claims claims = Jwts.claims().setSubject(email);
        claims.put("role", role);
        claims.put("userId", userId);
        claims.put("affiliationId", affiliationId);

        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + tokenValidity))
            .signWith(this.getKey(), SignatureAlgorithm.HS256)
            .compact();
    }
}
