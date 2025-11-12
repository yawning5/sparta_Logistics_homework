package com.keepgoing.order.presentation.dto.response.base;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // 공통 (xx00~xx09)
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "5000", "서버 내부 오류입니다."),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "5400", "요청 형식이 올바르지 않습니다."),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "5400", "요청 값이 유효하지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "5405", "허용되지 않은 HTTP 메서드입니다."),

    // 인증(401) / 인가(403)
    UNAUTHORIZED_NO_TOKEN(HttpStatus.UNAUTHORIZED, "5401", "인증 토큰이 없습니다."),
    UNAUTHORIZED_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "5401", "토큰이 만료되었습니다."),
    UNAUTHORIZED_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "5401", "유효하지 않은 토큰입니다."),
    FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "5403", "접근 권한이 없습니다."),

    // 주문 (HTTP 코드 매핑)
    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "5404", "해당 주문을 찾을 수 없습니다."),
    ORDER_INVALID_STATE(HttpStatus.CONFLICT, "5409", "유효하지 않은 주문 상태입니다."),
    ORDER_STATE_UPDATE_NOT_ALLOWED(HttpStatus.CONFLICT, "5409", "현재 상태에서는 변경할 수 없습니다."),
    ORDER_DELETION_NOT_ALLOWED(HttpStatus.UNPROCESSABLE_ENTITY, "5422", "현재 주문을 삭제할 수 없습니다."),
    ORDER_CANCEL_NOT_ALLOWED(HttpStatus.UNPROCESSABLE_ENTITY, "5422", "현재 주문을 취소할 수 없습니다."),
    ORDER_CANCEL_FAIL(HttpStatus.UNPROCESSABLE_ENTITY, "5424", "주문 취소 요청에 실패했습니다."),

    // 재고·상품
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "5504", "해당 상품을 찾을 수 없습니다."),
    INVENTORY_RESERVATION_FAIL(HttpStatus.UNPROCESSABLE_ENTITY, "5522", "재고 예약에 실패했습니다."),

    // 외부 통신 (Gateway/Upstream)
    FEIGN_REQUEST_FAIL(HttpStatus.BAD_GATEWAY, "5602", "하위 서비스로 요청이 실패했습니다."),
    FEIGN_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "5604", "하위 서비스 응답이 지연되었습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
