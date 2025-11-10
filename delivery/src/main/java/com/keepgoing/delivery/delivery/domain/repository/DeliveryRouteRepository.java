package com.keepgoing.delivery.delivery.domain.repository;

import com.keepgoing.delivery.delivery.domain.entity.DeliveryRoute;
import com.keepgoing.delivery.delivery.domain.entity.DeliveryRouteStatus;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryRouteRepository {
    void save(DeliveryRoute route);

    void saveAll(List<DeliveryRoute> routes);

    Optional<DeliveryRoute> findByIdAndIsDeletedFalse(UUID id);

    // 특정 배송의 모든 경로 조회
    List<DeliveryRoute> findByDeliveryId(UUID deliveryId);
    List<DeliveryRoute> findByDeliveryIdAndIsDeletedFalse(UUID deliveryId);

    // 특정 배송의 경로를 순서대로 조회
    List<DeliveryRoute> findByDeliveryIdOrderByRouteSeq(UUID deliveryId);

    // 배송담당자의 경로 목록 조회
    List<DeliveryRoute> findByDeliveryPersonIdAndIsDeletedFalse(Long deliveryPersonId);

    // 배송담당자의 진행 중인 경로 조회
    List<DeliveryRoute> findByDeliveryPersonIdAndStatus(Long deliveryPersonId, DeliveryRouteStatus status);

    // 허브별 경로 조회
    List<DeliveryRoute> findByDepartureHubIdAndIsDeletedFalse(UUID departureHubId);
    List<DeliveryRoute> findByArrivalHubIdAndIsDeletedFalse(UUID arrivalHubId);

}