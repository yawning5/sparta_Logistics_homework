package com.keepgoing.delivery.deliveryperson.infrastructure.persistence.mapper;

import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPerson;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliverySeq;
import com.keepgoing.delivery.deliveryperson.infrastructure.persistence.entity.DeliveryPersonEntity;
import org.springframework.stereotype.Component;


@Component
public class DeliveryPersonMapper {

    public DeliveryPersonEntity toJpaEntity(DeliveryPerson domain) {
        return new DeliveryPersonEntity(
                domain.getId(),
                domain.getHubId(),
                domain.getSlackId(),
                domain.getType(),
                domain.getDeliverySeq().value(),
                domain.isDeleted(),
                domain.getDeletedAt(),
                domain.getDeletedBy(),
                null,
                null
        );
    }

    public DeliveryPerson toDomainEntity(DeliveryPersonEntity jpa) {
        return DeliveryPerson.reconstruct(
                jpa.getId(),
                jpa.getHubId(),
                jpa.getSlackId(),
                jpa.getType(),
                new DeliverySeq(jpa.getDeliverySeq()),
                jpa.isDeleted(),
                jpa.getDeletedAt(),
                jpa.getDeletedBy()
        );
    }

}