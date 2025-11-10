package com.keepgoing.product.presentation.controller;


import com.keepgoing.product.application.command.CreateProductCommand;
import com.keepgoing.product.application.command.DeleteProductCommand;
import com.keepgoing.product.application.command.GetProductCommand;
import com.keepgoing.product.application.command.UpdateProductCommand;
import com.keepgoing.product.application.dto.ProductResult;
import com.keepgoing.product.application.service.ProductService;
import com.keepgoing.product.infrastructure.security.CustomUserDetails;
import com.keepgoing.product.presentation.dto.BaseResponseDTO;
import com.keepgoing.product.presentation.dto.reqeust.CreateProductRequestDTO;
import com.keepgoing.product.presentation.dto.reqeust.UpdateRequestProductDTO;
import com.keepgoing.product.presentation.dto.response.ProductResponseDTO;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        @RequestBody CreateProductRequestDTO request) {

        CreateProductCommand command = CreateProductCommand.of(user, request);

        ProductResult result = productService.createProduct(command);

        ProductResponseDTO response = ProductResponseDTO.from(result);

        return ResponseEntity.ok(BaseResponseDTO.success(response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'HUB', 'DELIVERY', 'COMPANY')")
    public ResponseEntity<?> getProduct(@AuthenticationPrincipal CustomUserDetails user,
        @PathVariable UUID id) {
        GetProductCommand command = GetProductCommand.of(user, id);
        ProductResult result = productService.getProduct(command);
        return ResponseEntity.ok(BaseResponseDTO.success(result));
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'HUB', 'COMPANY')")
    public ResponseEntity<?> updateProduct(@AuthenticationPrincipal CustomUserDetails user,
        @PathVariable UUID id,
        @RequestBody UpdateRequestProductDTO request) {
        UpdateProductCommand command = UpdateProductCommand.of(id, user, request);
        ProductResult result = productService.updateProduct(command);
        return ResponseEntity.ok(BaseResponseDTO.success(result));
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasAnyRole('MASTER', 'HUB')")
    public ResponseEntity<?> deleteProduct(@AuthenticationPrincipal CustomUserDetails user,
        @PathVariable UUID id) {
        DeleteProductCommand command = DeleteProductCommand.of(id, user);
        productService.deleteProduct(command);
        return ResponseEntity.ok(BaseResponseDTO.ok());
    }
}
