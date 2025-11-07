package com.sparta.vendor.infrastructure.config;

import com.sparta.vendor.infrastructure.provider.JwtTokenProvider;
import com.sparta.vendor.infrastructure.security.CustomAccessDeniedHandler;
import com.sparta.vendor.infrastructure.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final CustomAccessDeniedHandler accessDeniedHandler;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/v1/vendors/health-check").permitAll()
                .requestMatchers("/v1/vendors/**").authenticated()
                .anyRequest().permitAll()
            )
            .exceptionHandling((exceptions) -> {
                exceptions
                    .accessDeniedHandler(accessDeniedHandler);
            })
            .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
