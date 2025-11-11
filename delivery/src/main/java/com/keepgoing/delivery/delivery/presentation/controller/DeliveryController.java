package com.keepgoing.delivery.delivery.presentation.controller;

import com.keepgoing.delivery.delivery.application.dto.request.CompleteRouteRequest;
import com.keepgoing.delivery.delivery.application.dto.request.CreateDeliveryRequest;
import com.keepgoing.delivery.delivery.application.dto.response.DeliveryResponse;
import com.keepgoing.delivery.delivery.application.dto.response.DeliveryRouteResponse;
import com.keepgoing.delivery.delivery.application.service.DeliveryService;
import com.keepgoing.delivery.delivery.domain.entity.Delivery;
import com.keepgoing.delivery.delivery.domain.entity.DeliveryRoute;
import com.keepgoing.delivery.delivery.domain.entity.DeliveryStatus;
import com.keepgoing.delivery.global.BaseResponseDto;
import com.keepgoing.delivery.global.RequireRole;
import com.keepgoing.delivery.global.security.UserContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;
    private final UserContextHolder userContextHolder;

    // 배송 생성
    @RequireRole({"MASTER"})
    @PostMapping
    public ResponseEntity<BaseResponseDto<DeliveryResponse>> createDelivery(
            @RequestBody CreateDeliveryRequest request
    ) {
        Delivery delivery = deliveryService.createDelivery(
                request.orderId(),
                request.departureHubId(),
                request.destinationHubId(),
                request.toAddress(),
                request.recipientUserId(),
                request.recipientSlackId()
        );

        DeliveryResponse response = DeliveryResponse.from(delivery);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponseDto.success(response));
    }

    // 배송 조회 (배송 id)
    @RequireRole({"HUB", "MASTER", "Delivery", "COMPANY"})
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDto<DeliveryResponse>> getDelivery(
            @PathVariable UUID id
    ) {
        Delivery delivery = deliveryService.findDeliveryByIdWithRoutes(id);
        DeliveryResponse response = DeliveryResponse.from(delivery);
        return ResponseEntity.ok(BaseResponseDto.success(response));
    }

    // 배송 조회 (주문 id)
    @RequireRole({"HUB", "MASTER", "Delivery", "COMPANY"})
    @GetMapping("/{orderId}")
    public ResponseEntity<BaseResponseDto<DeliveryResponse>> getDeliveryByOrderId(
            @PathVariable UUID orderId
    ) {
        Delivery delivery = deliveryService.findDeliveryByOrderId(orderId);
        DeliveryResponse response = DeliveryResponse.fromWithoutRoutes(delivery);
        return ResponseEntity.ok(BaseResponseDto.success(response));
    }

    // 배송 상태 별 목록 조회
    @RequireRole({"HUB", "MASTER"})
    @GetMapping("/search")
    public ResponseEntity<BaseResponseDto<List<DeliveryResponse>>> getDeliveriesByStatus(
            @RequestParam DeliveryStatus status
    ) {
        List<Delivery> deliveries = deliveryService.findDeliveriesByStatus(status);
        List<DeliveryResponse> responses = deliveries.stream()
                .map(DeliveryResponse::fromWithoutRoutes)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponseDto.success(responses));
    }

    // 허브 간 배송 시작
    @RequireRole({"HUB", "MASTER", "Delivery"})
    @PostMapping("/{id}/start-hub-delivery")
    public ResponseEntity<BaseResponseDto<Void>> startHubDelivery(
            @PathVariable UUID id
    ) {
        deliveryService.startHubDelivery(id);
        return ResponseEntity.ok(BaseResponseDto.success(null));
    }

    // 허브 간 경로 완료
    @RequireRole({"HUB", "MASTER", "Delivery"})
    @PostMapping("/{id}/complete-route")
    public ResponseEntity<BaseResponseDto<Void>> completeHubRoute(
            HttpServletRequest httpServletRequest,
            @PathVariable UUID id,
            @RequestBody CompleteRouteRequest request
    ) {

        String userRole = userContextHolder.getUserRole(httpServletRequest);
        UUID hubId = userContextHolder.getUserHubId(httpServletRequest);

        deliveryService.completeHubRoute(
                id,
                request.routeSeq(),
                request.toDistance(),
                request.toDuration(),
                userRole,
                hubId
        );
        return ResponseEntity.ok(BaseResponseDto.success(null));
    }

    // 업체 배송 시작
    @RequireRole({"HUB", "MASTER", "Delivery"})
    @PostMapping("/{id}/start-vendor-delivery")
    public ResponseEntity<BaseResponseDto<Void>> startVendorDelivery(
            HttpServletRequest httpServletRequest,
            @PathVariable UUID id
    ) {
        String userRole = userContextHolder.getUserRole(httpServletRequest);
        Long userId = userContextHolder.getUserId(httpServletRequest);

        deliveryService.startVendorDelivery(id, userId, userRole);
        return ResponseEntity.ok(BaseResponseDto.success(null));
    }

    // 업체 배송 완료(배송 완료)
    @RequireRole({"HUB", "MASTER", "Delivery"})
    @PostMapping("/{id}/complete")
    public ResponseEntity<BaseResponseDto<Void>> completeDelivery(
            @PathVariable UUID id
    ) {
        deliveryService.completeDelivery(id);
        return ResponseEntity.ok(BaseResponseDto.success(null));
    }

    // 배송 취소
    @RequireRole({"HUB", "MASTER"})
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseDto<Void>> cancelDelivery(
            @PathVariable UUID id,
            @RequestParam String deletedBy
    ) {
        deliveryService.cancelDelivery(id, deletedBy);
        return ResponseEntity.ok(BaseResponseDto.success(null));
    }

    // 특정 배송의 경로 조회
    @RequireRole({"HUB", "MASTER"})
    @GetMapping("/{id}/routes/search")
    public ResponseEntity<BaseResponseDto<List<DeliveryRouteResponse>>> getRoutes(
            @PathVariable UUID id
    ) {
        List<DeliveryRoute> routes = deliveryService.findRoutesByDeliveryId(id);
        List<DeliveryRouteResponse> responses = routes.stream()
                .map(DeliveryRouteResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponseDto.success(responses));
    }

    // 배송 담당자 별 배송 경로 조회
    @RequireRole({"HUB", "MASTER"})
    @GetMapping("/{deliveryPersonId}/routes/search")
    public ResponseEntity<BaseResponseDto<List<DeliveryRouteResponse>>> getRoutesByDeliveryPerson(
            @PathVariable Long deliveryPersonId
    ) {
        List<DeliveryRoute> routes = deliveryService.findRoutesByDeliveryPerson(deliveryPersonId);
        List<DeliveryRouteResponse> responses = routes.stream()
                .map(DeliveryRouteResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponseDto.success(responses));
    }
}