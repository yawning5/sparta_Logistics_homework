package com.keepgoing.order.presentation.client;

import com.keepgoing.order.presentation.dto.response.client.ProductSearchResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "product-service")
public interface ProductClient {

    @GetMapping("/v1/internal/products/{id}")
    ProductSearchResponse getProduct(@PathVariable(name = "id") String id);
}
