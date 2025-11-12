package com.keepgoing.delivery.global.aop;

import com.keepgoing.delivery.global.RequireRole;
import com.keepgoing.delivery.global.exception.BusinessException;
import com.keepgoing.delivery.global.exception.ErrorCode;
import com.keepgoing.delivery.global.security.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleCheckAspect {

    private final UserContextHolder userContextHolder;

    @Around("@annotation(requireRole)")
    public Object checkRole(ProceedingJoinPoint joinPoint, RequireRole requireRole) throws Throwable {

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            throw new BusinessException(ErrorCode.USER_UNAUTHORIZED);
        }

        HttpServletRequest request = attributes.getRequest();
        String userRole = userContextHolder.getUserRole(request);

        if (!Arrays.asList(requireRole.value()).contains(userRole)) {
            throw new BusinessException(ErrorCode.USER_FORBIDDEN);
        }

        return joinPoint.proceed();
    }
}
