package com.keepgoing.delivery.deliveryperson.domain.entity;

import com.keepgoing.delivery.global.exception.BusinessException;
import com.keepgoing.delivery.global.exception.ErrorCode;

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
    private Long deletedBy;

    private DeliveryPerson(Long userId, UUID hubId, String slackId, DeliveryPersonType type, DeliverySeq seq) {
        if (userId == null)
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_REQUIRED_USER_ID);
        if (type == null)
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_REQUIRED_TYPE);
        if (slackId == null || slackId.isBlank())
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_REQUIRED_SLACK_ID);
        if (type == DeliveryPersonType.VENDOR && hubId == null)
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_VENDOR_HUB_REQUIRED);

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
            Long deletedBy
    ) {
        DeliveryPerson deliveryPerson = new DeliveryPerson(id, hubId, slackId, type, deliverySeq);
        deliveryPerson.isDeleted = isDeleted;
        deliveryPerson.deletedAt = deletedAt;
        deliveryPerson.deletedBy = deletedBy;
        return deliveryPerson;
    }

    // Getters
    public Long getId() { return id; }
    public UUID getHubId() { return hubId; }
    public String getSlackId() { return slackId; }
    public DeliveryPersonType getType() { return type; }
    public DeliverySeq getDeliverySeq() { return deliverySeq; }
    public boolean isDeleted() { return isDeleted; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public Long getDeletedBy() { return deletedBy; }

    public void updateSlackId(String slackId) {
        if (slackId == null || slackId.isBlank())
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_REQUIRED_SLACK_ID);
        this.slackId = slackId;
    }

    public void changeHub(UUID hubId) {
        if (this.type != DeliveryPersonType.VENDOR) {
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_CANNOT_CHANGE_HUB);
        }
        if (hubId == null) {
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_INVALID_HUB_ID);
        }
        this.hubId = hubId;
    }

    public void changeType(DeliveryPersonType newType, UUID newHubId) {
        if (newType == null) {
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_REQUIRED_NEW_TYPE);
        }

        if (this.type == newType) {
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_INVALID_TYPE_CHANGE);
        }

        // HUB → VENDOR
        if (newType == DeliveryPersonType.VENDOR) {
            if (newHubId == null) {
                throw new BusinessException(ErrorCode.DELIVERY_PERSON_HUB_REQUIRED_FOR_VENDOR_CONVERT);
            }
            this.type = DeliveryPersonType.VENDOR;
            this.hubId = newHubId;
        }
        // VENDOR → HUB
        else {
            this.type = DeliveryPersonType.HUB;
            this.hubId = null;
        }
    }

    public void markDeleted(Long deletedBy) {
        if (this.isDeleted) {
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_ALREADY_DELETED);
        }
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
    }
}