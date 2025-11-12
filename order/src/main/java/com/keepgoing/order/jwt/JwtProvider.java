package com.keepgoing.order.jwt;

import java.util.List;
import org.springframework.security.core.Authentication;

public interface JwtProvider {
    TokenStatus validateToken(String token);

    String getEmail(String token);

    List<String> getRoles(String token);

    String getUserId(String token);

    String getAffiliationId(String token);

    Authentication getAuthentication(String token);

    String createAccessToken(String email, String role, String userId, String affiliationId);
}
