package com.keepgoing.order.application.event.stock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class StockService {

    private final StockRepository stockRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer decreaseStock(UUID productId, int quantity) {

        Stock stock = stockRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("상품을 찾을 수 없습니다"));

        stock.decreaseStock(quantity);

        stockRepository.save(stock);

        log.info("재고 차감 완료 - 상품 ID: {}, 차감 수량: {}", productId, quantity);

        return stock.getStock();
    }
}
