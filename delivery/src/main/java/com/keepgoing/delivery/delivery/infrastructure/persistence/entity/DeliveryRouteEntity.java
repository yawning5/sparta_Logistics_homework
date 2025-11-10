package com.keepgoing.delivery.delivery.infrastructure.persistence.entity;

import com.keepgoing.delivery.delivery.domain.entity.DeliveryRouteStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "delivery_routes")
@Getter
@NoArgsConstructor
public class DeliveryRouteEntity {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "delivery_id", nullable = false)
    private UUID deliveryId;

    @Column(name = "route_seq", nullable = false)
    private Integer routeSeq;

    @Column(name = "departure_hub_id", nullable = false)
    private UUID departureHubId;

    @Column(name = "arrival_hub_id", nullable = false)
    private UUID arrivalHubId;

    @Column(name = "expected_distance_km", nullable = false)
    private Double expectedDistanceKm;

    @Column(name = "expected_time_minutes", nullable = false)
    private Integer expectedTimeMinutes;

    @Column(name = "actual_distance_km")
    private Double actualDistanceKm;

    @Column(name = "actual_time_minutes")
    private Integer actualTimeMinutes;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DeliveryRouteStatus status;

    @Column(name = "delivery_person_id")
    private Long deliveryPersonId;

    // soft delete
    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    // auditing
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public DeliveryRouteEntity(
            UUID id,
            UUID deliveryId,
            Integer routeSeq,
            UUID departureHubId,
            UUID arrivalHubId,
            Double expectedDistanceKm,
            Integer expectedTimeMinutes,
            Double actualDistanceKm,
            Integer actualTimeMinutes,
            DeliveryRouteStatus status,
            Long deliveryPersonId,
            boolean deleted,
            LocalDateTime deletedAt,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        this.id = id;
        this.deliveryId = deliveryId;
        this.routeSeq = routeSeq;
        this.departureHubId = departureHubId;
        this.arrivalHubId = arrivalHubId;
        this.expectedDistanceKm = expectedDistanceKm;
        this.expectedTimeMinutes = expectedTimeMinutes;
        this.actualDistanceKm = actualDistanceKm;
        this.actualTimeMinutes = actualTimeMinutes;
        this.status = status;
        this.deliveryPersonId = deliveryPersonId;
        this.deleted = deleted;
        this.deletedAt = deletedAt;
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