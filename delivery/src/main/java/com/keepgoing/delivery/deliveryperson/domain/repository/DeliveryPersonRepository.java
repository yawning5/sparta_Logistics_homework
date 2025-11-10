package com.keepgoing.delivery.deliveryperson.domain.repository;

import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPerson;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPersonType;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface DeliveryPersonRepository {
    DeliveryPerson save(DeliveryPerson deliveryPerson);

    Optional<DeliveryPerson> findByIdAndIsDeletedFalse(Long id);

    List<DeliveryPerson> findByTypeAndIsDeletedFalse(DeliveryPersonType type);

    List<DeliveryPerson> findByTypeAndHubIdAndIsDeletedFalse(DeliveryPersonType type, UUID hubId);

    int countByTypeAndHubId(DeliveryPersonType type, UUID hubId);
    int findMaxSeqByTypeAndHubId(DeliveryPersonType type, UUID hubId);
}