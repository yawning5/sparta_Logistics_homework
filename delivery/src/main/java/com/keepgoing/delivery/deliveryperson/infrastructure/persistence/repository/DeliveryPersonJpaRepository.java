package com.keepgoing.delivery.deliveryperson.infrastructure.persistence.repository;

import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPersonType;
import com.keepgoing.delivery.deliveryperson.infrastructure.persistence.entity.DeliveryPersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DeliveryPersonJpaRepository extends JpaRepository<DeliveryPersonEntity, Long> {

    Optional<DeliveryPersonEntity> findByIdAndDeletedFalse(Long id);

    List<DeliveryPersonEntity> findByTypeAndDeletedFalse(DeliveryPersonType type);

    List<DeliveryPersonEntity> findByTypeAndHubIdAndDeletedFalse(DeliveryPersonType type, UUID hubId);

    int countByTypeAndHubId(DeliveryPersonType type, UUID hubId);

    @Query("SELECT COALESCE(MAX(dp.deliverySeq), 0) FROM DeliveryPersonEntity dp " +
            "WHERE dp.type = :type AND (:hubId IS NULL OR dp.hubId = :hubId)")
    Integer findMaxSeqByTypeAndHubId(@Param("type") DeliveryPersonType type, @Param("hubId") UUID hubId);
}