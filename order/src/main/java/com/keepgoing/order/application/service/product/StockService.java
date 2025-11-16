package com.keepgoing.order.application.service.product;

import com.keepgoing.order.domain.product.Product;
import com.keepgoing.order.infrastructure.product.ProductRepository;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final ProductRepository productRepository;

    @Transactional
    public void decreaseStock(UUID productId, int quantity) {
        log.info("재고 차감 시작");
        Product product = productRepository.findById(productId)
            .orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다"));

        product.decreaseStock(quantity);
        productRepository.save(product);

        // 재고 차감 완료 로그
        log.info("재고 차감 완료 - 상품 ID: {}, 차감 수량: {}", productId, quantity);
    }
}
