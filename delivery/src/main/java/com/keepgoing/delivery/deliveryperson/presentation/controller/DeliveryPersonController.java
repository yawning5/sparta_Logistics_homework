package com.keepgoing.delivery.deliveryperson.presentation.controller;

import com.keepgoing.delivery.deliveryperson.application.dto.request.ChangeHubRequest;
import com.keepgoing.delivery.deliveryperson.application.dto.request.ChangeTypeRequest;
import com.keepgoing.delivery.deliveryperson.application.dto.request.RegisterDeliveryPersonRequest;
import com.keepgoing.delivery.deliveryperson.application.dto.request.UpdateSlackIdRequest;
import com.keepgoing.delivery.deliveryperson.application.dto.response.DeliveryPersonResponse;
import com.keepgoing.delivery.deliveryperson.application.service.DeliveryPersonService;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPerson;
import com.keepgoing.delivery.deliveryperson.domain.entity.DeliveryPersonType;
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
@RequestMapping("/v1/delivery-persons")
@RequiredArgsConstructor
public class DeliveryPersonController {

    private final DeliveryPersonService deliveryPersonService;
    private final UserContextHolder userContextHolder;

    // 회원 가입 시 배솓 담당자 등록
    @RequireRole({"HUB", "MASTER"})
    @PostMapping
    public ResponseEntity<BaseResponseDto<DeliveryPersonResponse>> registerDeliveryPerson(
            HttpServletRequest httpServletRequest,
            @RequestBody RegisterDeliveryPersonRequest request
    ) {

        String userRole = userContextHolder.getUserRole(httpServletRequest);
        UUID userHubId = userContextHolder.getUserHubId(httpServletRequest);

        DeliveryPerson deliveryPerson = deliveryPersonService.registerDeliveryPerson(
                request.userId(),
                request.slackId(),
                request.type(),
                request.hubId(),
                userRole,
                userHubId
        );

        DeliveryPersonResponse response = DeliveryPersonResponse.from(deliveryPerson);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponseDto.success(response));
    }

    // 배송 담당자 조회
    @RequireRole({"HUB", "MASTER", "DELIVERY"})
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDto<DeliveryPersonResponse>> getDeliveryPerson(
            HttpServletRequest httpServletRequest,
            @PathVariable Long id
    ) {
        Long userId = userContextHolder.getUserId(httpServletRequest);
        String userRole = userContextHolder.getUserRole(httpServletRequest);

        DeliveryPerson deliveryPerson = deliveryPersonService.findDeliveryPerson(id, userId, userRole);
        DeliveryPersonResponse response = DeliveryPersonResponse.from(deliveryPerson);
        return ResponseEntity.ok(BaseResponseDto.success(response));
    }

    // 배송 담당자 타입 별 조회
    @RequireRole({"MASTER"})
    @GetMapping("/search")
    public ResponseEntity<BaseResponseDto<List<DeliveryPersonResponse>>> getDeliveryPersonsByType(
            @RequestParam DeliveryPersonType type
    ) {
        List<DeliveryPerson> deliveryPersons = deliveryPersonService.findDeliveryPersonsByType(type);
        List<DeliveryPersonResponse> responses = deliveryPersons.stream()
                .map(DeliveryPersonResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponseDto.success(responses));
    }

    // 허브 별 배송 담당자 목록 조회
    @RequireRole({"HUB", "MASTER"})
    @GetMapping("/{hubId}/search")
    public ResponseEntity<BaseResponseDto<List<DeliveryPersonResponse>>> getVendorDeliveryPersonsByHub(
            HttpServletRequest httpServletRequest,
            @PathVariable UUID hubId
    ) {
        String userRole = userContextHolder.getUserRole(httpServletRequest);
        UUID userHubId = userContextHolder.getUserHubId(httpServletRequest);

        List<DeliveryPerson> deliveryPersons = deliveryPersonService.findVendorDeliveryPersonsByHub(hubId, userRole, userHubId);
        List<DeliveryPersonResponse> responses = deliveryPersons.stream()
                .map(DeliveryPersonResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponseDto.success(responses));
    }

    // slackId 수정
    @RequireRole({"HUB", "MASTER"})
    @PatchMapping("/{id}/slack-id")
    public ResponseEntity<BaseResponseDto<Void>> updateSlackId(
            HttpServletRequest httpServletRequest,
            @PathVariable Long id,
            @RequestBody UpdateSlackIdRequest request
    ) {
        String userRole = userContextHolder.getUserRole(httpServletRequest);
        UUID userHubId = userContextHolder.getUserHubId(httpServletRequest);

        deliveryPersonService.updateSlackId(id, request.slackId(), userRole, userHubId);
        return ResponseEntity.ok(BaseResponseDto.success(null));
    }

    // 업체 배송 담당자 소속 허브 변경
    @RequireRole({"HUB", "MASTER"})
    @PatchMapping("/{id}/hub")
    public ResponseEntity<BaseResponseDto<Void>> changeHub(
            HttpServletRequest httpServletRequest,
            @PathVariable Long id,
            @RequestBody ChangeHubRequest request
    ) {
        String userRole = userContextHolder.getUserRole(httpServletRequest);
        UUID userHubId = userContextHolder.getUserHubId(httpServletRequest);

        deliveryPersonService.changeHub(id, request.hubId(), userRole, userHubId);
        return ResponseEntity.ok(BaseResponseDto.success(null));
    }

    // 배송 담당자 타입 변경
    @RequireRole({"HUB", "MASTER"})
    @PatchMapping("/{id}/type")
    public ResponseEntity<BaseResponseDto<Void>> changeType(
            HttpServletRequest httpServletRequest,
            @PathVariable Long id,
            @RequestBody ChangeTypeRequest request
    ) {
        String userRole = userContextHolder.getUserRole(httpServletRequest);
        UUID userHubId = userContextHolder.getUserHubId(httpServletRequest);

        deliveryPersonService.changeType(id, request.newType(), request.newHubId(), userRole, userHubId);
        return ResponseEntity.ok(BaseResponseDto.success(null));
    }

    // 배송 담당자 삭제
    @RequireRole({"HUB", "MASTER"})
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseDto<Void>> deleteDeliveryPerson(
            HttpServletRequest httpServletRequest,
            @PathVariable Long id
    ) {
        Long userId = userContextHolder.getUserId(httpServletRequest);
        String userRole = userContextHolder.getUserRole(httpServletRequest);
        UUID userHubId = userContextHolder.getUserHubId(httpServletRequest);

        deliveryPersonService.softDelete(id, userId, userRole, userHubId);
        return ResponseEntity.ok(BaseResponseDto.success(null));
    }
}