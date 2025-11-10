package com.keepgoing.delivery.delivery.infrastructure.persistence.repository;

import com.keepgoing.delivery.delivery.domain.entity.DeliveryRouteStatus;
import com.keepgoing.delivery.delivery.infrastructure.persistence.entity.DeliveryRouteEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryRouteJpaRepository extends JpaRepository<DeliveryRouteEntity, UUID> {

    Optional<DeliveryRouteEntity> findByIdAndDeletedFalse(UUID id);

    List<DeliveryRouteEntity> findByDeliveryId(UUID deliveryId);

    List<DeliveryRouteEntity> findByDeliveryIdAndDeletedFalse(UUID deliveryId);

    List<DeliveryRouteEntity> findByDeliveryIdOrderByRouteSeq(UUID deliveryId);


    List<DeliveryRouteEntity> findByDeliveryPersonIdAndDeletedFalse(Long deliveryPersonId);

    List<DeliveryRouteEntity> findByDeliveryPersonIdAndStatus(Long deliveryPersonId, DeliveryRouteStatus status);


    List<DeliveryRouteEntity> findByDepartureHubIdAndDeletedFalse(UUID departureHubId);


    List<DeliveryRouteEntity> findByArrivalHubIdAndDeletedFalse(UUID arrivalHubId);

}