package com.keepgoing.delivery.delivery.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Delivery {
    private final UUID id;
    private final UUID orderId;
    private DeliveryStatus status;
    private final UUID departureHubId;
    private final UUID destinationHubId;
    private final Address address;
    private final Long recipientUserId;
    private final String recipientSlackId;
    private Long vendorDeliveryPersonId;
    private final List<DeliveryRoute> routes = new ArrayList<>();

    private Delivery(UUID orderId,
                     UUID departureHubId,
                     UUID destinationHubId,
                     Address address,
                     Long recipientUserId,
                     String recipientSlackId) {

        if (orderId == null) throw new IllegalArgumentException("orderId는 필수입니다.");
        if (departureHubId == null) throw new IllegalArgumentException("출발 허브 ID는 필수입니다.");
        if (destinationHubId == null) throw new IllegalArgumentException("목적지 허브 ID는 필수입니다.");
        if (address == null) throw new IllegalArgumentException("배송지 주소는 필수입니다.");
        if (recipientUserId == null) throw new IllegalArgumentException("수령인 ID는 필수입니다.");
        if (recipientSlackId == null || recipientSlackId.isBlank())
            throw new IllegalArgumentException("수령인 Slack ID는 필수입니다.");

        this.id = UUID.randomUUID();
        this.orderId = orderId;
        this.departureHubId = departureHubId;
        this.destinationHubId = destinationHubId;
        this.address = address;
        this.recipientUserId = recipientUserId;
        this.recipientSlackId = recipientSlackId;
        this.status = DeliveryStatus.HUB_WAITING;
    }

    public static Delivery create(UUID orderId,
                                  UUID departureHubId,
                                  UUID destinationHubId,
                                  Address address,
                                  Long recipientUserId,
                                  String recipientSlackId) {
        return new Delivery(orderId, departureHubId, destinationHubId, address, recipientUserId, recipientSlackId);
    }


    public UUID getOrderId() { return orderId; }
    public DeliveryStatus getStatus() { return status; }
    public List<DeliveryRoute> getRoutes() { return routes; }

    public void addRoute(DeliveryRoute route) {
        if (route == null) {
            throw new IllegalArgumentException("경로 정보는 null일 수 없습니다");
        }
        routes.add(route);
    }
}
