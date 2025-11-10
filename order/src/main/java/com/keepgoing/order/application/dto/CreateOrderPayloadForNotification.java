package com.keepgoing.order.application.dto;

// 알림쪽에서 필요로 하는 정보

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 주문 번호
 * 주문자 정보 : receiverId (식별자)
 * 주문자 아이디 :
 * 주문 시간 :
 * 상품 이름 :
 * 납품 기한 :
 * 요청 사항 :
 * 도착지 :
 */
public record CreateOrderPayloadForNotification(
    UUID orderId,
    String supplierName,
    String receiverName,
    String productName,
    Integer quantity,
    LocalDateTime orderedAt,
    LocalDateTime deliveryDueAt,
    String deliveryRequestNote
) {

}
