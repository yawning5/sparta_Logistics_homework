package com.keepgoing.gateway.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import java.util.concurrent.TimeUnit;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {


    @Bean
    public Cache<String, Boolean> logoutTokenCache() {
        return Caffeine.newBuilder()
            // TODO: 테스트 이후 JWT 만료기간보다 1분정도 더 길게 설정
            .expireAfterWrite(31, TimeUnit.MINUTES)
            .maximumSize(10_000)
            .build();
    }
}