package com.keepgoing.delivery.delivery.domain.entity;

import java.util.UUID;

public class Delivery {
    private UUID Id;
    private UUID orderId;
    private DeliveryStatus status;
    private UUID departureHubId;
    private UUID destinationHubId;
    private Address address;
    private Long recipientUserId;
    private String recipientSlackId;
    private Long vendorDeliveryPersonId;
}
