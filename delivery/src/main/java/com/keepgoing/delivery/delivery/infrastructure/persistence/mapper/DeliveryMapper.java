package com.keepgoing.delivery.delivery.infrastructure.persistence.mapper;

import com.keepgoing.delivery.delivery.domain.entity.Delivery;
import com.keepgoing.delivery.delivery.domain.vo.Address;
import com.keepgoing.delivery.delivery.infrastructure.persistence.entity.AddressJpa;
import com.keepgoing.delivery.delivery.infrastructure.persistence.entity.DeliveryEntity;
import org.springframework.stereotype.Component;

@Component
public class DeliveryMapper {

    public DeliveryEntity toJpaEntity(Delivery domain) {
        AddressJpa addressEmbeddable = new AddressJpa(
                domain.getAddress().street(),
                domain.getAddress().city(),
                domain.getAddress().zipcode()
        );

        return new DeliveryEntity(
                domain.getId(),
                domain.getOrderId(),
                domain.getStatus(),
                domain.getDepartureHubId(),
                domain.getDestinationHubId(),
                addressEmbeddable,
                domain.getRecipientUserId(),
                domain.getRecipientSlackId(),
                domain.getVendorDeliveryPersonId(),
                domain.isDeleted(),
                domain.getDeletedAt(),
                domain.getDeletedBy(),
                domain.getCreatedAt(),
                domain.getUpdatedAt()
        );
    }

    public Delivery toDomainEntity(DeliveryEntity jpa) {
        Address address = new Address(
                jpa.getAddress().getStreet(),
                jpa.getAddress().getCity(),
                jpa.getAddress().getZipcode()
        );

        return Delivery.reconstruct(
                jpa.getId(),
                jpa.getOrderId(),
                jpa.getStatus(),
                jpa.getDepartureHubId(),
                jpa.getDestinationHubId(),
                address,
                jpa.getRecipientUserId(),
                jpa.getRecipientSlackId(),
                jpa.getVendorDeliveryPersonId(),
                jpa.isDeleted(),
                jpa.getDeletedAt(),
                jpa.getDeletedBy(),
                jpa.getCreatedAt(),
                jpa.getUpdatedAt()
        );
    }
}