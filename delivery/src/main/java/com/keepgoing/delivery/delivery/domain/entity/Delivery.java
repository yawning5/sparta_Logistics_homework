package com.keepgoing.delivery.delivery.domain.entity;

import com.keepgoing.delivery.delivery.domain.vo.Address;
import com.keepgoing.delivery.delivery.domain.vo.Distance;
import com.keepgoing.delivery.delivery.domain.vo.Duration;
import lombok.Getter;

import java.time.LocalDateTime;
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

    // 경로 목록은 메모리에서만 관리 (조회 시 별도 로딩)
    private final List<DeliveryRoute> routes = new ArrayList<>();
    private boolean isDeleted = false;
    private LocalDateTime deletedAt;
    private String deletedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Delivery create(UUID orderId,
                                  UUID departureHubId,
                                  UUID destinationHubId,
                                  Address address,
                                  Long recipientUserId,
                                  String recipientSlackId) {
        return new Delivery(orderId, departureHubId, destinationHubId, address, recipientUserId, recipientSlackId);
    }

    public static Delivery reconstruct(
            UUID id,
            UUID orderId,
            DeliveryStatus status,
            UUID departureHubId,
            UUID destinationHubId,
            Address address,
            Long recipientUserId,
            String recipientSlackId,
            Long vendorDeliveryPersonId,
            boolean isDeleted,
            LocalDateTime deletedAt,
            String deletedBy,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return new Delivery(
                orderId,
                departureHubId,
                destinationHubId,
                address,
                recipientUserId,
                recipientSlackId
        );
    }

    public UUID getId() { return id; }
    public UUID getOrderId() { return orderId; }
    public DeliveryStatus getStatus() { return status; }
    public UUID getDepartureHubId() { return departureHubId; }
    public UUID getDestinationHubId() { return destinationHubId; }
    public Address getAddress() { return address; }
    public Long getRecipientUserId() { return recipientUserId; }
    public String getRecipientSlackId() { return recipientSlackId; }
    public Long getVendorDeliveryPersonId() { return vendorDeliveryPersonId; }
    public List<DeliveryRoute> getRoutes() { return new ArrayList<>(routes); }  // 방어적 복사
    public boolean isDeleted() { return isDeleted; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public String getDeletedBy() {return deletedBy;}
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }



    // 경로 추가 (내부 리스트에만 추가, 실제 저장은 Service에서 처리)
    public void addRoute(DeliveryRoute route) {
        if (route == null) {
            throw new IllegalArgumentException("경로 정보는 null일 수 없습니다");
        }
        routes.add(route);
    }

    // 경로 목록 로딩 (Repository에서 조회 후 설정)
    public void loadRoutes(List<DeliveryRoute> routes) {
        this.routes.clear();
        this.routes.addAll(routes);
    }

    public void startFromHub() {
        if (status != DeliveryStatus.HUB_WAITING)
            throw new IllegalStateException("HUB_WAITING 상태에서만 출발할 수 있습니다.");
        this.status = DeliveryStatus.HUB_IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }

    public void arriveAtDestHub() {
        if (status != DeliveryStatus.HUB_IN_PROGRESS)
            throw new IllegalStateException("HUB_IN_PROGRESS 상태에서만 도착 허브에 도착할 수 있습니다.");
        this.status = DeliveryStatus.DEST_HUB_ARRIVED;
        this.updatedAt = LocalDateTime.now();
    }

    public void startVendorDelivery() {
        if (status != DeliveryStatus.DEST_HUB_ARRIVED)
            throw new IllegalStateException("DEST_HUB_ARRIVED 상태에서만 가맹점 배송을 시작할 수 있습니다.");
        this.status = DeliveryStatus.VENDOR_IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }

    public void completeDelivery() {
        if (status != DeliveryStatus.VENDOR_IN_PROGRESS)
            throw new IllegalStateException("VENDOR_IN_PROGRESS 상태에서만 배송을 완료할 수 있습니다.");
        this.status = DeliveryStatus.DELIVERED;
        this.updatedAt = LocalDateTime.now();
    }

    public void assignVendorDeliveryPerson(Long vendorDeliveryPersonId) {
        if (vendorDeliveryPersonId == null)
            throw new IllegalArgumentException("업체 배송담당자 ID는 필수입니다.");
        if (this.status != DeliveryStatus.DEST_HUB_ARRIVED)
            throw new IllegalStateException("목적지 허브 도착 후에만 업체 배송담당자를 배정할 수 있습니다.");
        this.vendorDeliveryPersonId = vendorDeliveryPersonId;
        this.updatedAt = LocalDateTime.now();
    }

    // 특정 경로 시작
    public void startRoute(int routeSeqValue) {
        DeliveryRoute route = routes.stream()
                .filter(r -> r.getRouteSeq().value() == routeSeqValue)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 순서의 경로를 찾을 수 없습니다."));

        route.startMoving();
        this.updatedAt = LocalDateTime.now();
    }

    // 특정 경로 도착 처리
    public void completeRoute(int routeSeqValue, Distance actualDistance, Duration actualTime) {
        DeliveryRoute route = routes.stream()
                .filter(r -> r.getRouteSeq().value() == routeSeqValue)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("해당 순서의 경로를 찾을 수 없습니다."));

        route.markArrived();
        route.recordActual(actualDistance, actualTime);
        this.updatedAt = LocalDateTime.now();
    }

    public void markDeleted(String deletedBy) {
        if (this.isDeleted) {
            throw new IllegalStateException("이미 삭제된 배송입니다.");
        }
        if (this.status != DeliveryStatus.HUB_WAITING) {
            throw new IllegalStateException("배송 시작 전에만 삭제할 수 있습니다.");
        }
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
        this.updatedAt = LocalDateTime.now();
    }
}