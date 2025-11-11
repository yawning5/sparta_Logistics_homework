package com.sparta.product.presentation.controller;


import com.sparta.product.application.command.CreateProductCommand;
import com.sparta.product.application.command.DeleteProductCommand;
import com.sparta.product.application.command.GetProductCommand;
import com.sparta.product.application.command.SearchProductCommand;
import com.sparta.product.application.command.UpdateProductCommand;
import com.sparta.product.application.dto.ProductResult;
import com.sparta.product.application.service.ProductService;
import com.sparta.product.infrastructure.security.CustomUserDetails;
import com.sparta.product.presentation.dto.BaseResponseDTO;
import com.sparta.product.presentation.dto.PageResponseDTO;
import com.sparta.product.presentation.dto.reqeust.CreateProductRequestDTO;
import com.sparta.product.presentation.dto.reqeust.SearchProductRequest;
import com.sparta.product.presentation.dto.reqeust.UpdateRequestProductDTO;
import com.sparta.product.presentation.dto.response.ProductResponseDTO;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
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
        @Valid @RequestBody CreateProductRequestDTO request) {

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
        @Valid @RequestBody UpdateRequestProductDTO request) {
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

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('MASTER', 'HUB', 'DELIVERY', 'COMPANY')")
    public ResponseEntity<?> searchProducts(
        @AuthenticationPrincipal CustomUserDetails user,
        Pageable pageable,
        @ModelAttribute SearchProductRequest request
    ) {

        int pageSize = pageable.getPageSize();
        if (pageSize != 10 && pageSize != 30 && pageSize != 50) {
            pageSize = 10;
        }
        pageable = PageRequest.of(pageable.getPageNumber(), pageSize, pageable.getSort());


        SearchProductCommand command = SearchProductCommand.of(
            user,
            request.productId(),
            request.name(),
            request.description(),
            request.minPrice(),
            request.maxPrice(),
            request.vendorId(),
            request.hubId()
        );

        Page<ProductResult> productResultPage = productService.searchProducts(command,
            pageable);

        Page<ProductResponseDTO> responsePage = productResultPage.map(ProductResponseDTO::from);

        PageResponseDTO<ProductResponseDTO> pageResponseDTO = PageResponseDTO.from(responsePage);

        return ResponseEntity.ok(BaseResponseDTO.success(pageResponseDTO));
    }
}
