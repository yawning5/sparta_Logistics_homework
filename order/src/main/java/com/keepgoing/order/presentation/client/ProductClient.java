package com.keepgoing.order.presentation.client;

import com.keepgoing.order.config.external.FeignConfig;
import com.keepgoing.order.presentation.dto.response.client.ProductSearchResponse;
import java.util.UUID;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service", configuration = FeignConfig.class)
public interface ProductClient {

    @GetMapping("/v1/internal/products/{id}")
    ProductSearchResponse getProduct(@PathVariable(name = "id") UUID id);
}
