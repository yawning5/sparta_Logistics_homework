package com.sparta.product.infrastructure.external.client;

import com.sparta.product.infrastructure.external.dto.VendorResponseDTO;
import com.sparta.product.presentation.dto.BaseResponseDTO;

import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "vendor-service")
public interface VendorClient {

    @GetMapping("/v1/internal/vendors/{id}")
    BaseResponseDTO<VendorResponseDTO> getVendorById(@RequestHeader("Authorization") String token,
        @PathVariable UUID id);

}
