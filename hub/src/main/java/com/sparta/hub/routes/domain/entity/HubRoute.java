package com.sparta.hub.routes.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "p_hub_routes")
@Getter
@NoArgsConstructor
public class HubRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "origin_hub_id", nullable = false)
    private UUID originHubId;

    @Column(name = "destination_hub_id", nullable = false)
    private UUID destinationHubId;

    @Column(name = "transit_time", nullable = false)
    private int transitMinutes;

    @Column(name = "distance_km", nullable = false, precision = 7, scale = 2)
    private BigDecimal distanceKm;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    private HubRoute(UUID originHubId, UUID destinationHubId, int transitMinutes, BigDecimal distanceKm) {
        this.originHubId = originHubId;
        this.destinationHubId = destinationHubId;
        this.transitMinutes = transitMinutes;
        this.distanceKm = distanceKm;
    }

    public static HubRoute create(UUID originHubId, UUID destinationHubId, int transitMinutes, BigDecimal distanceKm) {
        return new HubRoute(originHubId, destinationHubId, transitMinutes, distanceKm);
    }

    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void update(int transitMinutes, BigDecimal distanceKm) {
        if (transitMinutes > 0) this.transitMinutes = transitMinutes;
        if (distanceKm != null && distanceKm.compareTo(BigDecimal.ZERO) > 0) this.distanceKm = distanceKm;
    }

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public void updateTransitMinutes(int transitMinutes) {
        if (transitMinutes <= 0)
            throw new IllegalArgumentException("운송 시간은 1분 이상이어야 합니다.");
        this.transitMinutes = transitMinutes;
    }

    public void updateDistanceKm(BigDecimal distanceKm) {
        if (distanceKm == null || distanceKm.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("거리는 0보다 커야 합니다.");
        this.distanceKm = distanceKm;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}