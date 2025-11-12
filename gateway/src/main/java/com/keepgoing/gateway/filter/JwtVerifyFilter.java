package com.keepgoing.gateway.filter;

import com.github.benmanes.caffeine.cache.Cache;
import com.keepgoing.gateway.dto.BaseResponseDto;
import com.keepgoing.gateway.dto.MemberResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtVerifyFilter implements Ordered, GlobalFilter {

    private final Cache<String, Boolean> logoutTokenCache;
    private final WebClient.Builder webClientBuilder;
    private final JwtDecoder jwtDecoder;
    private AntPathMatcher pathMatcher = new AntPathMatcher();

    private static final List<String> WHITE_LIST = List.of(
        "/v*/member/login",
        "/v*/member/signup",
        "/actuator/**"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var path = request.getURI().getPath();

        log.info("\n\n\njwt 확인 필터 진입\n\n\n");

        // 1. 화이트리스트 통과
        boolean isWhitelisted = WHITE_LIST.stream()
            .anyMatch(pattern -> pathMatcher.match(pattern, path));
        if (isWhitelisted) {
            return chain.filter(exchange);
        }

        // 2. JWT 확인
        String authHeader = request.getHeaders().getFirst("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete(); // jwt 토큰 없으면 내부서버 진입 불가
        }

        String token = authHeader.replace("Bearer ", "");

        log.info("\n\n\n\n카페인 캐시 확인지점\n\n\n\n");
        logoutTokenCache.asMap().forEach((k, v) -> {
            log.info("key={}, value={}", k, v);
        });

        // 캐시확인
        if (Boolean.TRUE.equals(logoutTokenCache.getIfPresent(token))) {
            log.info("로그아웃된 토큰 요청: {}", token);
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 3. 멤버 서버에 검증요청
        return webClientBuilder.build()
            .get()
            .uri("http://member-service/v1/internal/member")
            .header("Authorization", authHeader)
            .retrieve()
            /*
            TODO: BaseEntity 로 감싸져서 오는 코드 해석
                ParameterizedTypeReference 에 대한 공부 및 정리
             */
            .bodyToMono(new ParameterizedTypeReference<BaseResponseDto<MemberResponseDto>>() {})
            .flatMap(dto -> {
                boolean isValid = validateMemberClaims(token, dto.getData());
                log.info(dto.toString());
                if (!isValid) {
                    log.warn("Invalid member claims for member '{}'", dto.getData());
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    return exchange.getResponse().setComplete();
                }

                return chain.filter(exchange);
            })
            .onErrorResume( e -> {
                log.warn("JWT 검증 실패: {}", e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
            });
    }

    private boolean validateMemberClaims(String authHeader, MemberResponseDto member) {
        Jwt jwt = jwtDecoder.decode(authHeader);
        log.info("jwt: {}", jwt);
        String requestRole = jwt.getClaims().get("role").toString();
        String requestUserId = jwt.getClaims().get("userId").toString();
        log.info("requestUserId: {}", requestUserId);
        log.info("requestRole: {}", requestRole);

        return requestRole.equals(member.role())
            && requestUserId.equals(member.userId().toString())
            && member.status().equals("APPROVED");
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
