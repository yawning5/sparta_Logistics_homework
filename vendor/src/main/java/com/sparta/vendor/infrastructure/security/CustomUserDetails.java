package com.sparta.vendor.infrastructure.security;


import com.sparta.vendor.domain.vo.UserRole;
import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

    private final Long userId;
    private final UserRole role;
    private final UUID affiliationId;

    public CustomUserDetails(String userId, String role, String affiliationId) {
        this.userId = Long.parseLong(userId);
        this.role = UserRole.valueOf(role);
        this.affiliationId = affiliationId == null ? null : UUID.fromString(affiliationId);
    }

    public Long getUserId() {
        return userId;
    }

    public UserRole getRole() {
        return role;
    }

    public UUID getAffiliationId() {
        return affiliationId;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(() -> role.getAuthority());
    }

    @Override
    public String getPassword() {
        return "";
    }

    @Override
    public String getUsername() {
        return String.valueOf(userId);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
