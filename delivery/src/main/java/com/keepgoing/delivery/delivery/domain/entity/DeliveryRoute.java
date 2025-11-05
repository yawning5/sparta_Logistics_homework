package com.keepgoing.delivery.delivery.domain.entity;

import java.util.UUID;

public class DeliveryRoute {
    private UUID Id;
    private RouteSeq routeSeq;
    private UUID departureHubId;
    private UUID arrivalHubId;
    private Distance expectedDistance;
    private Duration expectedTime;
    private Distance actualDistance;
    private Duration actualTime;
    private DeliveryRouteStatus status;
    private Long deliveryPersonId;
    private Delivery delivery;
}
