package com.keepgoing.order.config.external;

import com.keepgoing.order.jwt.JwtProvider;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Configuration
@RequiredArgsConstructor
public class FeignConfig {

    private final JwtProvider jwtProvider;

    private volatile String cachedToken;
    private volatile long expiresAtMillis;

    @Value("${jwt.validity}")
    private long tokenValidity;


    @Bean
    public RequestInterceptor authHeaderRelayInterceptor() {
        return new RequestInterceptor() {
            @Override
            public void apply(RequestTemplate template) {
                ServletRequestAttributes attrs =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

                if (attrs != null) {
                    String auth = attrs.getRequest().getHeader("Authorization");
                    if (org.springframework.util.StringUtils.hasText(auth)) {
                        template.header("Authorization", auth);
                        return;
                    }
                }
                template.header("Authorization", getServiceToken());
            }
        };
    }

    private String getServiceToken() {
        long now = System.currentTimeMillis();
        if (cachedToken == null || now >= expiresAtMillis - 5_000) { // 만료 5초 전 갱신
            String email = "system1@system.com";
            String role = "MASTER";
            String userId = "0";
            String affiliationId = "20283a91-e391-4254-ba64-6fed6bc45d0d";
            cachedToken = jwtProvider.createAccessToken(email, role, userId, affiliationId);

            expiresAtMillis = now + tokenValidity;
        }
        return "Bearer " + cachedToken;
    }
}
