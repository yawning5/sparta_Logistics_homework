package com.keepgoing.member.infrastructure.jwt;


import com.keepgoing.member.infrastructure.userDetails.CustomUserDetails;

public interface JwtProvider {
    String generateAccessToken(CustomUserDetails user);
}
