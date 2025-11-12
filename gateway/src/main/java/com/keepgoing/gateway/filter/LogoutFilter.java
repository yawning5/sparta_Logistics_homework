package com.keepgoing.gateway.filter;

import com.github.benmanes.caffeine.cache.Cache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.util.PathMatcher;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class LogoutFilter implements GlobalFilter, Ordered {

    private final PathMatcher pathMatcher;
    private final JwtDecoder jwtDecoder;
    private final Cache<String, Boolean> logoutTokenCache;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 현재 요청 경로 추출
        var request = exchange.getRequest();
        String path = request.getURI().getPath();

        // 로그아웃 엔드포인트 확인
        if (pathMatcher.match("/v*/member/logout", path)) {
            String authHeader = request.getHeaders().getFirst("Authorization");

            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                return exchange.getResponse().setComplete();
            }

            String token = authHeader.replace("Bearer ", "");

            if (Boolean.TRUE.equals(logoutTokenCache.getIfPresent(token))) {
                log.info("이미 로그아웃된 토큰 요청: {}", token);
                exchange.getResponse().setStatusCode(HttpStatus.CONFLICT); // 409
                return exchange.getResponse().setComplete();
            }

            try {
                // 단순 claim 확인용 (안 터지면 정상 구조의 JWT)
                jwtDecoder.decode(token);

                // 블랙리스트 캐시에 등록
                logoutTokenCache.put(token, true);
                log.info("로그아웃 요청 처리 완료: 캐시에 토큰 등록됨");

                logoutTokenCache.asMap().forEach((k, v) -> {
                    log.info("key={}, value={}", k, v);
                });

                // 응답 종료 (라우팅 불필요)
                exchange.getResponse().setStatusCode(HttpStatus.OK);
                return exchange.getResponse().setComplete();

            } catch (Exception e) {
                // 구조 이상한 토큰만 400
                log.warn("잘못된 JWT 구조: {}", e.getMessage());
                exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
                return exchange.getResponse().setComplete();
            }
        }

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
