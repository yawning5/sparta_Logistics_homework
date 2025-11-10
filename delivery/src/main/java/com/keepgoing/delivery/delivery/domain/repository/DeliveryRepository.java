// DeliveryRepository.java 계속
package com.keepgoing.delivery.delivery.domain.repository;

import com.keepgoing.delivery.delivery.domain.entity.Delivery;
import com.keepgoing.delivery.delivery.domain.entity.DeliveryStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRepository {
    Delivery save(Delivery delivery);

    Optional<Delivery> findByIdAndIsDeletedFalse(UUID id);

    Optional<Delivery> findByOrderIdAndIsDeletedFalse(UUID orderId);

    List<Delivery> findByStatusAndIsDeletedFalse(DeliveryStatus status);

    // 허브 배송담당자의 배송 목록 (경로에서 배송담당자 ID로 조회)
    List<Delivery> findByHubDeliveryPersonUserId(Long deliveryPersonUserId);

    // 업체 배송담당자의 배송 목록
    List<Delivery> findByVendorDeliveryPersonIdAndIsDeletedFalse(Long vendorDeliveryPersonId);

    // 허브별 진행 중인 배송 목록
    List<Delivery> findInProgressDeliveriesByHub(UUID hubId);

    boolean existsByOrderId(UUID orderId);

}