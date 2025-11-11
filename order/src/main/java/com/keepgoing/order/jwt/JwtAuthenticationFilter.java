package com.keepgoing.order.jwt;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j(topic = "JwtAuthenticationFilter")
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private static final Set<String> EXCLUDE_URL_PATTERNS = Set.of(
        "/v3/api-docs/**",
        "/swagger-ui/**",
        "/swagger-ui.html",
        "/swagger-resources/**",
        "/webjars/**",
        "/actuator/health"
    );

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();

        return EXCLUDE_URL_PATTERNS.stream()
            .anyMatch(pattern -> PATH_MATCHER.match(pattern, path));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
        FilterChain filterChain) throws ServletException, IOException {
        String accessToken = extractAccessToken(request);

        if (accessToken == null || accessToken.isBlank()) {
            log.error("토큰이 비어있습니다. {}", accessToken);
            throw new AuthenticationCredentialsNotFoundException("토큰이 없습니다.");
        }

        TokenStatus tokenStatus = jwtProvider.validateToken(accessToken);

        if (tokenStatus.isValid()) {
            log.info("토큰 유효 {}", accessToken);

            setAuthentication(accessToken);

            filterChain.doFilter(request, response);
            return;
        }

        if (tokenStatus.isExpired()) {
            log.error("토큰이 만료되었습니다. {}", accessToken);
            throw new CredentialsExpiredException("토큰이 만료되었습니다.");

        }

        if (tokenStatus.isInvalid()) {
            log.error("토큰이 유효하지 않습니다. {}", accessToken);
            throw new BadCredentialsException("유효하지 않은 토큰입니다.");
        }
    }

    private void setAuthentication(String accessToken) {
        Authentication authentication = jwtProvider.getAuthentication(accessToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String extractAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }

        return null;
    }
}
