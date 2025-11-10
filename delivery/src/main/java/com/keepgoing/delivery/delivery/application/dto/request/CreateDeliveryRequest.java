package com.keepgoing.delivery.delivery.application.dto.request;

import com.keepgoing.delivery.delivery.domain.vo.Address;

import java.util.UUID;

public record CreateDeliveryRequest(
        UUID orderId,
        UUID departureHubId,
        UUID destinationHubId,
        AddressDto address,
        Long recipientUserId,
        String recipientSlackId
) {
    public Address toAddress() {
        return new Address(
                address.street(),
                address.city(),
                address.zipcode()
        );
    }
}