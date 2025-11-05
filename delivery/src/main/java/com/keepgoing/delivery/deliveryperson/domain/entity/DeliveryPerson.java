package com.keepgoing.delivery.deliveryperson.domain.entity;

import java.util.UUID;

public class DeliveryPerson {
    private final UUID id;
    private final Long userId;
    private UUID hubId;
    private final String slackId;
    private final DeliveryPersonType type;
    private final DeliverySeq deliverySeq;


    private DeliveryPerson(Long userId, UUID hubId, String slackId, DeliveryPersonType type, DeliverySeq seq) {
        if (userId == null) throw new IllegalArgumentException("userId는 필수입니다.");
        if (type == null) throw new IllegalArgumentException("type은 필수입니다.");
        if (slackId == null || slackId.isBlank()) throw new IllegalArgumentException("slackId는 필수입니다.");
        if (type == DeliveryPersonType.VENDOR && hubId == null)
            throw new IllegalArgumentException("업체 배송 담당자의 hubId는 필수입니다.");

        this.id = UUID.randomUUID();
        this.userId = userId;
        this.hubId = hubId;
        this.slackId = slackId;
        this.type = type;
        this.deliverySeq = seq;
    }

    public static DeliveryPerson createHubDeliveryPerson(Long userId, String slackId, DeliverySeq seq) {
        return new DeliveryPerson(userId, null, slackId, DeliveryPersonType.HUB, seq);
    }

    public static DeliveryPerson createVendorDeliveryPerson(Long userId, UUID hubId, String slackId, DeliverySeq seq) {
        return new DeliveryPerson(userId, hubId, slackId, DeliveryPersonType.VENDOR, seq);
    }

    public UUID getId() { return id; }
    public Long getUserId() { return userId; }
    public UUID getHubId() { return hubId; }
    public String getSlackId() { return slackId; }
    public DeliveryPersonType getType() { return type; }
    public DeliverySeq getDeliverySeq() { return deliverySeq; }

    public void assignToHub(UUID hubId) {
        if (type == DeliveryPersonType.HUB) {
            throw new IllegalStateException("허브 배송 담당자는 허브에 배정될 수 없습니다.");
        }
        if (hubId == null) throw new IllegalArgumentException("HubId는 필수입니다.");
        this.hubId = hubId;
    }
}
