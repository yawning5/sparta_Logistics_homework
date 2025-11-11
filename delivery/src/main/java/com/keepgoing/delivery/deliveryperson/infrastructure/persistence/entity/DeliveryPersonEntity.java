package com.keepgoing.delivery.deliveryperson.infrastructure.persistence.entity;

import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPersonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "delivery_persons")
@Getter
@NoArgsConstructor
public class DeliveryPersonEntity {

    @Id
    @Column(name = "id")
    private Long id;  // userId와 동일

    @Column(name = "hub_id")
    private UUID hubId;

    @Column(name = "slack_id", nullable = false)
    private String slackId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private DeliveryPersonType type;

    @Column(name = "delivery_seq", nullable = false)
    private Integer deliverySeq;

    @Column(name = "is_deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Column(name = "deleted_by")
    private Long deletedBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public DeliveryPersonEntity(
            Long id,
            UUID hubId,
            String slackId,
            DeliveryPersonType type,
            Integer deliverySeq,
            boolean isDeleted,
            LocalDateTime deletedAt,
            Long deletedBy,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.hubId = hubId;
        this.slackId = slackId;
        this.type = type;
        this.deliverySeq = deliverySeq;
        this.deleted = isDeleted;
        this.deletedAt = deletedAt;
        this.deletedBy = deletedBy;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}