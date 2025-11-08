package com.sparta.product.infrastructure.security;

import com.sparta.product.infrastructure.provider.JwtTokenProvider;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JwtFilter")
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain)
        throws ServletException, IOException {

        String tokenHeader = request.getHeader("Authorization");

        if (tokenHeader == null || !tokenHeader.startsWith("Bearer ")) {
            log.warn("잘못된 Authorization 헤더 형식: {}", tokenHeader);
            sendErrorResponse(response, 1,
                "Authorization 헤더가 없거나 형식이 잘못되었습니다. 'Bearer <token>' 형태여야 합니다.");
            return;
        }

        String token = tokenHeader.substring(7).trim();

        try {
            if (jwtTokenProvider.validateToken(token)) {
                Claims claims = jwtTokenProvider.parseToken(token);
                String userId = claims.get("userId", String.class);
                String role = claims.get("role", String.class);
                String affiliationId = claims.get("affiliationId", String.class);

                CustomUserDetails userDetails = new CustomUserDetails(userId, role, affiliationId, token);
                UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                log.warn("JWT 토큰이 유효하지 않음");
                sendErrorResponse(response, 1, "유효하지 않은 JWT 토큰입니다.");
                return;
            }
        } catch (Exception e) {
            log.error("JWT 인증 처리 중 예외 발생: {}", e.getMessage(), e);
            SecurityContextHolder.clearContext();
            sendErrorResponse(response, 1, "JWT 토큰 처리 중 오류가 발생했습니다.");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        return path.startsWith("/v1/vendors/health-check") || path.startsWith("/actuator/health");
    }

    private void sendErrorResponse(HttpServletResponse response, int code, String message)
        throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json;charset=UTF-8");
        String body = String.format("{\"error\": {\"code\": %d, \"message\": \"%s\"}}", code,
            message);
        response.getWriter().write(body);
    }
}
