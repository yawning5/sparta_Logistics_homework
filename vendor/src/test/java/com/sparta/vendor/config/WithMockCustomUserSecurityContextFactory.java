package com.keepgoing.vendor.config;

import com.keepgoing.vendor.infrastructure.security.CustomUserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory implements
    WithSecurityContextFactory<WithMockCustomUser> {

    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        String affiliationId = annotation.affiliationId().isEmpty() ? null : annotation.affiliationId();
        String token = annotation.token().isEmpty() ? null : annotation.token();


        CustomUserDetails principal = new CustomUserDetails(
            String.valueOf(annotation.id()),
            annotation.role().name(),
            affiliationId,
            token
        );

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, "",
            principal.getAuthorities());

        principal.getAuthorities()
            .forEach(authority -> System.out.println(authority.getAuthority() + " ssssss"));

        context.setAuthentication(auth);
        return context;
    }
}
