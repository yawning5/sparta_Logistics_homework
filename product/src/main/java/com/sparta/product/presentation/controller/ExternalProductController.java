package com.sparta.product.presentation.controller;


import com.sparta.product.application.command.CreateProductCommand;
import com.sparta.product.application.dto.ProductResult;
import com.sparta.product.application.service.ProductService;
import com.sparta.product.presentation.dto.BaseResponseDTO;
import com.sparta.product.presentation.dto.reqeust.CreateProductRequestDTO;
import com.sparta.product.presentation.dto.response.ProductResponseDTO;
import com.sparta.product.infrastructure.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/products")
public class ExternalProductController {

    private final ProductService productService;

    @GetMapping("/health-check")
    public String healthCheck() {
        return "OK";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MASTER', 'HUB', 'COMPANY')")
    public ResponseEntity<?> createProduct(@AuthenticationPrincipal CustomUserDetails user,
        @RequestBody CreateProductRequestDTO request){

        CreateProductCommand command = CreateProductCommand.of(user, request);

        ProductResult result = productService.createProduct(command);

        ProductResponseDTO response = ProductResponseDTO.from(result);

        return ResponseEntity.ok(BaseResponseDTO.success(response));
    }

}
