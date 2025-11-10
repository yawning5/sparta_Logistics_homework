package com.keepgoing.delivery.deliveryperson.domain.entity;

import java.time.LocalDateTime;
import java.util.UUID;

public class DeliveryPerson {
    private final Long id;
    private UUID hubId;
    private String slackId;
    private DeliveryPersonType type;
    private final DeliverySeq deliverySeq;
    private boolean isDeleted = false;
    private LocalDateTime deletedAt;
    private String deletedBy;

    private DeliveryPerson(Long userId, UUID hubId, String slackId, DeliveryPersonType type, DeliverySeq seq) {
        if (userId == null) throw new IllegalArgumentException("userId는 필수입니다.");
        if (type == null) throw new IllegalArgumentException("type은 필수입니다.");
        if (slackId == null || slackId.isBlank()) throw new IllegalArgumentException("slackId는 필수입니다.");
        if (type == DeliveryPersonType.VENDOR && hubId == null)
            throw new IllegalArgumentException("업체 배송 담당자의 hubId는 필수입니다.");

        this.id = userId;
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

    public static DeliveryPerson reconstruct(
            Long id,
            UUID hubId,
            String slackId,
            DeliveryPersonType type,
            DeliverySeq deliverySeq,
            boolean isDeleted,
            LocalDateTime deletedAt,
            String deletedBy
    ) {
        DeliveryPerson deliveryPerson = new DeliveryPerson(id, hubId, slackId, type, deliverySeq);
        deliveryPerson.isDeleted = isDeleted;
        deliveryPerson.deletedAt = deletedAt;
        deliveryPerson.deletedBy = deletedBy;
        return deliveryPerson;
    }

    public Long getId() { return id; }
    public UUID getHubId() { return hubId; }
    public String getSlackId() { return slackId; }
    public DeliveryPersonType getType() { return type; }
    public DeliverySeq getDeliverySeq() { return deliverySeq; }
    public boolean isDeleted() { return isDeleted; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public String getDeletedBy() { return deletedBy; }

    public void updateSlackId(String slackId) {
        if (slackId == null || slackId.isBlank())
            throw new IllegalArgumentException("slackId는 필수입니다.");
        this.slackId = slackId;
    }

    /**
     * 업체 배송 담당자의 소속 허브 변경
     * - VENDOR 타입만 가능
     */
    public void changeHub(UUID hubId) {
        if (this.type != DeliveryPersonType.VENDOR) {
            throw new IllegalStateException("업체 배송 담당자만 허브를 변경할 수 있습니다.");
        }
        if (hubId == null) {
            throw new IllegalArgumentException("허브 ID는 필수입니다.");
        }
        this.hubId = hubId;
    }

    /**
     * 배송 담당자 타입 변경
     * - HUB → VENDOR: 소속 허브 필수
     * - VENDOR → HUB: 소속 허브 제거
     */
    public void changeType(DeliveryPersonType newType, UUID newHubId) {
        if (newType == null) {
            throw new IllegalArgumentException("변경할 타입은 필수입니다.");
        }

        if (this.type == newType) {
            throw new IllegalArgumentException("동일한 타입으로는 변경할 수 없습니다.");
        }

        // HUB → VENDOR: 소속 허브 필수
        if (newType == DeliveryPersonType.VENDOR) {
            if (newHubId == null) {
                throw new IllegalArgumentException("업체 배송 담당자로 전환 시 소속 허브는 필수입니다.");
            }
            this.type = DeliveryPersonType.VENDOR;
            this.hubId = newHubId;
        }
        // VENDOR → HUB: 소속 허브 제거
        else {
            this.type = DeliveryPersonType.HUB;
            this.hubId = null;
        }
    }

    // soft delete
    public void markDeleted(String deletedBy) {
        if (this.isDeleted) {
            throw new IllegalStateException("이미 삭제된 배송 담당자입니다.");
        }
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }

}