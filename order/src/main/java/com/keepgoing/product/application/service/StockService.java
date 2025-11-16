package com.keepgoing.product.application.service;

import com.keepgoing.product.application.repository.ProductRepository;
import com.keepgoing.product.domain.Product;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j(topic = "StockService")
@Service
@RequiredArgsConstructor
public class StockService {
    private final ProductRepository productRepository;

    public void decreaseStock(UUID productId, int quantity) {
        Product product = productRepository.findById(productId);

        product.decreaseStock(quantity);

        productRepository.save(product);

        // 재고 차감 완료 로그
        log.info("재고 차감 완료 - 상품 ID: {}, 차감 수량: {}", productId, quantity);
    }
}
