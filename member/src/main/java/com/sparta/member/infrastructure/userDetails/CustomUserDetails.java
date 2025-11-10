package com.keepgoing.member.infrastructure.userDetails;

<<<<<<< HEAD
import com.keepgoing.member.infrastructure.persistence.jpa.entity.MemberJpa;
=======
import com.sparta.member.domain.enums.Status;
import com.sparta.member.infrastructure.persistence.jpa.entity.MemberJpa;
>>>>>>> 84b265aace245f24c5ee8f8823dd3a33829a6688
import java.util.Collection;
import java.util.List;
import java.util.UUID;
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

    public UUID getAffiliationId() {
        return member.getAffiliationId();
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
        return member.getStatus() == Status.APPROVED;
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
