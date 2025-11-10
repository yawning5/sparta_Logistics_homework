package com.sparta.product.presentation.controller;


import com.sparta.product.application.command.GetProductCommand;
import com.sparta.product.application.dto.ProductResult;
import com.sparta.product.application.service.ProductService;
import com.sparta.product.infrastructure.security.CustomUserDetails;
import com.sparta.product.presentation.dto.BaseResponseDTO;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/internal/products")
public class InternalProductController {

    private final ProductService productService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'HUB', 'DELIVERY', 'COMPANY')")
    public ResponseEntity<?> getProduct(@AuthenticationPrincipal CustomUserDetails user,
        @PathVariable UUID id) {
        GetProductCommand command = GetProductCommand.of(user, id);
        ProductResult result = productService.getProduct(command);
        return ResponseEntity.ok(BaseResponseDTO.success(result));
    }

}
