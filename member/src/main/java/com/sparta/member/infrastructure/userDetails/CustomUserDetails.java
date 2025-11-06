package com.sparta.member.infrastructure.userDetails;

import com.sparta.member.infrastructure.persistence.jpa.entity.MemberJpa;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private final MemberJpa member;

    public  CustomUserDetails(MemberJpa member) {
        this.member = member;
    }

    @Override
    public String getUsername() {
        return member.getEmail();
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    public Long getUserId() {
        return member.getId();
    }

    public String getRole() {
        return member.getRole().name();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(
            new SimpleGrantedAuthority("ROLE_" + member.getRole().name())
        );
    }

    // TODO: 계정의 승인 상태에 따라 false 값 반환하도록 변경
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
}
