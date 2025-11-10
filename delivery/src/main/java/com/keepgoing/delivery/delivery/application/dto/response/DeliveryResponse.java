package com.keepgoing.delivery.delivery.application.dto.response;

import com.keepgoing.delivery.delivery.domain.entity.Delivery;
import com.keepgoing.delivery.delivery.domain.entity.DeliveryStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record DeliveryResponse(
        UUID id,
        UUID orderId,
        DeliveryStatus status,
        UUID departureHubId,
        UUID destinationHubId,
        AddressResponse address,
        Long recipientUserId,
        String recipientSlackId,
        Long vendorDeliveryPersonId,
        List<DeliveryRouteResponse> routes,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static DeliveryResponse from(Delivery delivery) {
        return new DeliveryResponse(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getStatus(),
                delivery.getDepartureHubId(),
                delivery.getDestinationHubId(),
                AddressResponse.from(delivery.getAddress()),
                delivery.getRecipientUserId(),
                delivery.getRecipientSlackId(),
                delivery.getVendorDeliveryPersonId(),
                delivery.getRoutes().stream()
                        .map(DeliveryRouteResponse::from)
                        .toList(),
                delivery.getCreatedAt(),
                delivery.getUpdatedAt()
        );
    }

    public static DeliveryResponse fromWithoutRoutes(Delivery delivery) {
        return new DeliveryResponse(
                delivery.getId(),
                delivery.getOrderId(),
                delivery.getStatus(),
                delivery.getDepartureHubId(),
                delivery.getDestinationHubId(),
                AddressResponse.from(delivery.getAddress()),
                delivery.getRecipientUserId(),
                delivery.getRecipientSlackId(),
                delivery.getVendorDeliveryPersonId(),
                null,
                delivery.getCreatedAt(),
                delivery.getUpdatedAt()
        );
    }
}