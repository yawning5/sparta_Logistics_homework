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

    // 회원 가입 시 배솓 담당자 등록
    @PostMapping
    public ResponseEntity<BaseResponseDto<DeliveryPersonResponse>> registerDeliveryPerson(
            @RequestBody RegisterDeliveryPersonRequest request
    ) {
        DeliveryPerson deliveryPerson = deliveryPersonService.registerDeliveryPerson(
                request.userId(),
                request.slackId(),
                request.type(),
                request.hubId()
        );

        DeliveryPersonResponse response = DeliveryPersonResponse.from(deliveryPerson);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponseDto.success(response));
    }

    // 배송 담당자 조회
    @GetMapping("/{id}")
    public ResponseEntity<BaseResponseDto<DeliveryPersonResponse>> getDeliveryPerson(
            @PathVariable Long id
    ) {
        DeliveryPerson deliveryPerson = deliveryPersonService.findDeliveryPerson(id);
        DeliveryPersonResponse response = DeliveryPersonResponse.from(deliveryPerson);
        return ResponseEntity.ok(BaseResponseDto.success(response));
    }

    // 배송 담당자 타입 별 조회
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
    @GetMapping("/{hubId}/search")
    public ResponseEntity<BaseResponseDto<List<DeliveryPersonResponse>>> getVendorDeliveryPersonsByHub(
            @PathVariable UUID hubId
    ) {
        List<DeliveryPerson> deliveryPersons = deliveryPersonService.findVendorDeliveryPersonsByHub(hubId);
        List<DeliveryPersonResponse> responses = deliveryPersons.stream()
                .map(DeliveryPersonResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(BaseResponseDto.success(responses));
    }

    // slackId 수정
    @PatchMapping("/{id}/slack-id")
    public ResponseEntity<BaseResponseDto<Void>> updateSlackId(
            @PathVariable Long id,
            @RequestBody UpdateSlackIdRequest request
    ) {
        deliveryPersonService.updateSlackId(id, request.slackId());
        return ResponseEntity.ok(BaseResponseDto.success(null));
    }

    // 업체 배송 담당자 소속 허브 변경
    @PatchMapping("/{id}/hub")
    public ResponseEntity<BaseResponseDto<Void>> changeHub(
            @PathVariable Long id,
            @RequestBody ChangeHubRequest request
    ) {
        deliveryPersonService.changeHub(id, request.hubId());
        return ResponseEntity.ok(BaseResponseDto.success(null));
    }

    // 배송 담당자 타입 변경
    @PatchMapping("/{id}/type")
    public ResponseEntity<BaseResponseDto<Void>> changeType(
            @PathVariable Long id,
            @RequestBody ChangeTypeRequest request
    ) {
        deliveryPersonService.changeType(id, request.newType(), request.newHubId());
        return ResponseEntity.ok(BaseResponseDto.success(null));
    }

    // 배송 담당자 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponseDto<Void>> deleteDeliveryPerson(
            @PathVariable Long id,
            @RequestParam String deletedBy
    ) {
        deliveryPersonService.softDelete(id, deletedBy);
        return ResponseEntity.ok(BaseResponseDto.success(null));
    }
}