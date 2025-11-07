package com.sparta.member.infrastructure.userDetails;

import com.sparta.member.domain.repository.MemberRepository;
import com.sparta.member.infrastructure.persistence.jpa.entity.MemberJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        MemberJpa m = memberRepository.findByEmailUseInfra(email);
        return new CustomUserDetails(m);
    }
}
