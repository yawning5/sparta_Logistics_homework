package com.sparta.product.infrastructure.config;


import com.sparta.product.infrastructure.provider.JwtTokenProvider;
import com.sparta.product.infrastructure.security.CustomAccessDeniedHandler;
import com.sparta.product.infrastructure.security.JwtAuthenticationFilter;
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
                .requestMatchers("/v1/products/health-check").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/v1/products/**").authenticated()
                .requestMatchers("/v1/internal/products/**").authenticated()
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
