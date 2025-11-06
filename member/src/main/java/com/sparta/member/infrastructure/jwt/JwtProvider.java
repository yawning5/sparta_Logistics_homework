package com.sparta.member.infrastructure.jwt;


import com.sparta.member.infrastructure.userDetails.CustomUserDetails;

public interface JwtProvider {
    String generateAccessToken(CustomUserDetails user);
}
