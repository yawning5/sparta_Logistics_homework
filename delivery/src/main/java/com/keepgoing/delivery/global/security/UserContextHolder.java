package com.keepgoing.delivery.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keepgoing.delivery.global.exception.BusinessException;
import com.keepgoing.delivery.global.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Map;
import java.util.UUID;

@Component
public class UserContextHolder {

    private static final String AUTH_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private final ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, Object> extractClaims(HttpServletRequest request) {
        String authHeader = request.getHeader(AUTH_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            throw new BusinessException(ErrorCode.USER_UNAUTHORIZED);
        }

        String token = authHeader.substring(BEARER_PREFIX.length());
        String[] parts = token.split("\\.");
        if (parts.length != 3) {
            throw new BusinessException(ErrorCode.USER_UNAUTHORIZED);
        }

        try {
            String payload = new String(Base64.getUrlDecoder().decode(parts[1]));
            return objectMapper.readValue(payload, Map.class);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.USER_UNAUTHORIZED);
        }
    }

    public Long getUserId(HttpServletRequest request) {
        Map<String, Object> claims = extractClaims(request);
        Object userId = claims.get("userId");
        if (userId == null) {
            throw new BusinessException(ErrorCode.USER_UNAUTHORIZED);
        }
        return Long.parseLong(userId.toString());
    }

    public String getUserRole(HttpServletRequest request) {
        Map<String, Object> claims = extractClaims(request);
        Object role = claims.get("role");
        if (role == null) {
            throw new BusinessException(ErrorCode.USER_UNAUTHORIZED);
        }
        return role.toString();
    }

    public UUID getUserHubId(HttpServletRequest request) {
        Map<String, Object> claims = extractClaims(request);
        Object affiliationId = claims.get("affiliationId");
        if (affiliationId == null) {
            return null; // MASTER 등은 허브(affiliation)가 없을 수 있음
        }
        return UUID.fromString(affiliationId.toString());
    }

    public String getEmail(HttpServletRequest request) {
        Map<String, Object> claims = extractClaims(request);
        Object email = claims.get("sub");
        return email != null ? email.toString() : null;
    }
}
