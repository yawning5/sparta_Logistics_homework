package com.sparta.practiceorder.stock.service;

import com.sparta.practiceorder.stock.domain.Product;
import com.sparta.practiceorder.stock.repository.ProductRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StockService {
    private final ProductRepository productRepository;

    public void decreaseStock(UUID productId, int quantity) {
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다"));

        product.decreaseStock(quantity);
        productRepository.save(product);

        // 재고 차감 완료 로그
        log.info("재고 차감 완료 - 상품 ID: {}, 차감 수량: {}", productId, quantity);
    }
}
