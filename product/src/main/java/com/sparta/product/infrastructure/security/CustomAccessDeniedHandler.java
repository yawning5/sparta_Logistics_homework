package com.sparta.product.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.sparta.product.presentation.dto.BaseResponseDTO;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException accessDeniedException)
        throws IOException, ServletException {

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json;charset=UTF-8");

        var errorResponse = BaseResponseDTO.error("FORBIDDEN", "권한이 없습니다.");

        response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
    }
}
