package com.sparta.vendor.config;

import com.sparta.vendor.domain.vo.UserRole;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    long id() default 1L;

    UserRole role() default UserRole.MASTER;

    String affiliationId() default "";

    String token() default "";
}
