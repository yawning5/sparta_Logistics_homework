package com.keepgoing.delivery.delivery.domain.entity;

import com.keepgoing.delivery.delivery.domain.vo.Address;
import com.keepgoing.delivery.delivery.domain.vo.Distance;
import com.keepgoing.delivery.delivery.domain.vo.Duration;
import com.keepgoing.delivery.global.exception.BusinessException;
import com.keepgoing.delivery.global.exception.ErrorCode;

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

    // soft delete
    private boolean isDeleted = false;
    private LocalDateTime deletedAt;
    private String deletedBy;

    // auditing
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Delivery(UUID orderId,
                     UUID departureHubId,
                     UUID destinationHubId,
                     Address address,
                     Long recipientUserId,
                     String recipientSlackId) {

        if (orderId == null)
            throw new BusinessException(ErrorCode.DELIVERY_INVALID_ORDER_ID);
        if (departureHubId == null)
            throw new BusinessException(ErrorCode.DELIVERY_INVALID_HUB);
        if (destinationHubId == null)
            throw new BusinessException(ErrorCode.DELIVERY_INVALID_HUB);
        if (address == null)
            throw new BusinessException(ErrorCode.DELIVERY_REQUIRED_FIELDS_MISSING);
        if (recipientUserId == null)
            throw new BusinessException(ErrorCode.DELIVERY_REQUIRED_FIELDS_MISSING);
        if (recipientSlackId == null || recipientSlackId.isBlank())
            throw new BusinessException(ErrorCode.DELIVERY_REQUIRED_FIELDS_MISSING);

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

    private Delivery(
            UUID id,
            UUID orderId,
            DeliveryStatus status,
            UUID departureHubId,
            UUID destinationHubId,
            Address address,
            Long recipientUserId,
            String recipientSlackId,
            Long vendorDeliveryPersonId
    ) {
        this.id = id;
        this.orderId = orderId;
        this.status = status;
        this.departureHubId = departureHubId;
        this.destinationHubId = destinationHubId;
        this.address = address;
        this.recipientUserId = recipientUserId;
        this.recipientSlackId = recipientSlackId;
        this.vendorDeliveryPersonId = vendorDeliveryPersonId;
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
            boolean deleted,
            LocalDateTime deletedAt,
            String deletedBy,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        Delivery delivery = new Delivery(id, orderId, status, departureHubId, destinationHubId, address,
                recipientUserId, recipientSlackId, vendorDeliveryPersonId);
        delivery.isDeleted = deleted;
        delivery.deletedAt = deletedAt;
        delivery.deletedBy = deletedBy;
        delivery.createdAt = createdAt;
        delivery.updatedAt = updatedAt;
        return delivery;
    }

    // Getters
    public UUID getId() { return id; }
    public UUID getOrderId() { return orderId; }
    public DeliveryStatus getStatus() { return status; }
    public UUID getDepartureHubId() { return departureHubId; }
    public UUID getDestinationHubId() { return destinationHubId; }
    public Address getAddress() { return address; }
    public Long getRecipientUserId() { return recipientUserId; }
    public String getRecipientSlackId() { return recipientSlackId; }
    public Long getVendorDeliveryPersonId() { return vendorDeliveryPersonId; }
    public List<DeliveryRoute> getRoutes() { return new ArrayList<>(routes); }
    public boolean isDeleted() { return isDeleted; }
    public LocalDateTime getDeletedAt() { return deletedAt; }
    public String getDeletedBy() { return deletedBy; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // 경로 추가
    public void addRoute(DeliveryRoute route) {
        if (route == null) {
            throw new BusinessException(ErrorCode.DELIVERY_INVALID_ROUTE);
        }
        routes.add(route);
    }

    // 경로 목록 로딩
    public void loadRoutes(List<DeliveryRoute> routes) {
        this.routes.clear();
        this.routes.addAll(routes);
    }

    // 상태 전이 메서드들
    public void startFromHub() {
        if (status != DeliveryStatus.HUB_WAITING)
            throw new BusinessException(ErrorCode.DELIVERY_CAN_START_ONLY_FROM_HUB_WAITING);
        this.status = DeliveryStatus.HUB_IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }

    public void arriveAtDestHub() {
        if (status != DeliveryStatus.HUB_IN_PROGRESS)
            throw new BusinessException(ErrorCode.DELIVERY_CAN_ARRIVE_DEST_ONLY_FROM_HUB_IN_PROGRESS);
        this.status = DeliveryStatus.DEST_HUB_ARRIVED;
        this.updatedAt = LocalDateTime.now();
    }

    public void startVendorDelivery() {
        if (status != DeliveryStatus.DEST_HUB_ARRIVED)
            throw new BusinessException(ErrorCode.DELIVERY_CAN_START_VENDOR_ONLY_FROM_DEST_HUB_ARRIVED);
        this.status = DeliveryStatus.VENDOR_IN_PROGRESS;
        this.updatedAt = LocalDateTime.now();
    }

    public void completeDelivery() {
        if (status != DeliveryStatus.VENDOR_IN_PROGRESS)
            throw new BusinessException(ErrorCode.DELIVERY_CAN_COMPLETE_ONLY_FROM_VENDOR_IN_PROGRESS);
        this.status = DeliveryStatus.DELIVERED;
        this.updatedAt = LocalDateTime.now();
    }

    public void assignVendorDeliveryPerson(Long vendorDeliveryPersonId) {
        if (vendorDeliveryPersonId == null)
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_REQUIRED_DELIVERY_ID);
        if (this.status != DeliveryStatus.DEST_HUB_ARRIVED)
            throw new BusinessException(ErrorCode.DELIVERY_PERSON_ASSIGN_ONLY_AFTER_DEST_ARRIVED);
        this.vendorDeliveryPersonId = vendorDeliveryPersonId;
        this.updatedAt = LocalDateTime.now();
    }

    public void startRoute(int routeSeqValue) {
        DeliveryRoute route = routes.stream()
                .filter(r -> r.getRouteSeq().value() == routeSeqValue)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_ROUTE_NOT_FOUND));

        route.startMoving();
        this.updatedAt = LocalDateTime.now();
    }

    public void completeRoute(int routeSeqValue, Distance actualDistance, Duration actualTime) {
        DeliveryRoute route = routes.stream()
                .filter(r -> r.getRouteSeq().value() == routeSeqValue)
                .findFirst()
                .orElseThrow(() -> new BusinessException(ErrorCode.DELIVERY_ROUTE_NOT_FOUND));

        route.markArrived();
        route.recordActual(actualDistance, actualTime);
        this.updatedAt = LocalDateTime.now();
    }

    public void markDeleted(String deletedBy) {
        if (this.isDeleted) {
            throw new BusinessException(ErrorCode.DELIVERY_ALREADY_DELETED);
        }
        if (this.status != DeliveryStatus.HUB_WAITING) {
            throw new BusinessException(ErrorCode.DELIVERY_DELETE_ONLY_BEFORE_START);
        }
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
        this.deletedBy = deletedBy;
        this.updatedAt = LocalDateTime.now();
    }
}