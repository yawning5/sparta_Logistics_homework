package com.keepgoing.delivery.delivery.infrastructure.persistence.repository;

import com.keepgoing.delivery.delivery.domain.entity.DeliveryStatus;
import com.keepgoing.delivery.delivery.infrastructure.persistence.entity.DeliveryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryJpaRepository extends JpaRepository<DeliveryEntity, UUID> {

    Optional<DeliveryEntity> findByIdAndDeletedFalse(UUID id);

    Optional<DeliveryEntity> findByOrderIdAndDeletedFalse(UUID orderId);

    List<DeliveryEntity> findByStatusAndDeletedFalse(DeliveryStatus status);

    List<DeliveryEntity> findByVendorDeliveryPersonIdAndDeletedFalse(Long vendorDeliveryPersonId);

    @Query("SELECT d FROM DeliveryEntity d " +
            "WHERE (d.departureHubId = :hubId OR d.destinationHubId = :hubId) " +
            "AND d.deleted = false")
    List<DeliveryEntity> findByDepartureHubIdOrDestinationHubIdAndDeletedFalse(@Param("hubId") UUID hubId);

    @Query("SELECT d FROM DeliveryEntity d " +
            "WHERE (d.departureHubId = :hubId OR d.destinationHubId = :hubId) " +
            "AND d.status IN ('HUB_IN_PROGRESS', 'DEST_HUB_ARRIVED', 'VENDOR_IN_PROGRESS') " +
            "AND d.deleted = false")
    List<DeliveryEntity> findInProgressDeliveriesByHub(@Param("hubId") UUID hubId);

    boolean existsByOrderId(UUID orderId);
}