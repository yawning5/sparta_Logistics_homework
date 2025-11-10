package com.keepgoing.member.application.service;

<<<<<<< HEAD
import com.keepgoing.member.application.dto.LoginDto;
import com.keepgoing.member.infrastructure.jwt.JwtProvider;
import com.keepgoing.member.infrastructure.userDetails.CustomUserDetails;
=======
import com.sparta.member.interfaces.dto.LoginDto;
import com.sparta.member.infrastructure.jwt.JwtProvider;
import com.sparta.member.infrastructure.userDetails.CustomUserDetails;
>>>>>>> 84b265aace245f24c5ee8f8823dd3a33829a6688
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final AuthenticationManager authManager;

    private final JwtProvider jwtProvider;

    public String login(LoginDto loginDto) {
        try {
            Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginDto.email(),
                    loginDto.password())
            );
            CustomUserDetails user = (CustomUserDetails) auth.getPrincipal();
            return jwtProvider.generateAccessToken(user);
        } catch (AuthenticationException e) {
            log.warn("Login failed(AuthService): {}", e.getMessage());
            throw new RuntimeException("Login failed");
        }
    }
}
