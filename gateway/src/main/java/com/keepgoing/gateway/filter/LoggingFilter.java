package com.keepgoing.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

// 모든 요청을 잡아 로깅하는 로깅 필터
@Component
@Slf4j
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    // Mono -> 0개 또는 1개의 데이터를 비동기적으로 발행
    // Flux -> 0개 이상 여러 데이터를 발행
    // 둘다 WebFlux 에서 사용하는 Mono, Flux 형태의 비동기 스트림
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        var request = exchange.getRequest();
        var path = request.getURI().getPath();
        var method = request.getMethod();

        log.info("[GatewayFilter-req] {} {}", method, path);

        // 체인 다음 필터로 요청 전달
        return chain.filter(exchange)
            .then(Mono.fromRunnable(() -> {
                var response = exchange.getResponse();
                log.info("[GatewayFilter-res] Response status: {}", response.getStatusCode());
            }));
    }

    @Override
    public int getOrder() {
        // 필터 실행 순서 낮을수록 먼저 실행 음수여도 상관 x
        return -1;
    }
}
