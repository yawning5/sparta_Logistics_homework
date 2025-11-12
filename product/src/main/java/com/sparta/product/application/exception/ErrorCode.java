package com.sparta.product.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "000", "서버 내부 오류입니다."),
    INVALID_JSON_FORMAT(HttpStatus.BAD_REQUEST, "001", "요청 형식이 올바르지 않습니다."),
    FORBIDDEN_HUB_OPERATION(HttpStatus.FORBIDDEN, "4001", "허브 관리자는 담당 허브에만 상품을 등록할 수 있습니다."),
    FORBIDDEN_COMPANY_OPERATION(HttpStatus.FORBIDDEN, "4002", "업체 담당자는 담당 업체에만 상품을 등록할 수 있습니다."),
    VENDOR_NOT_FOUND(HttpStatus.NOT_FOUND, "4003", "존재하지 않는 업체 입니다."),
    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "4004", "존재하지 않는 상품 입니다."),
    PRODUCT_DELETED(HttpStatus.GONE, "4005", "삭제된 상품 입니다."),
    FORBIDDEN_HUB_GET_OPERATION(HttpStatus.FORBIDDEN, "4006", "허브 관리자는 담당 허브의 상품만 조회할 수 있습니다."),
    FORBIDDEN_HUB_UPDATE_OPERATION(HttpStatus.FORBIDDEN, "4007",
        "허브관리자는 담당 허브에 등록된 상품만 수정할 수 있습니다."),
    FORBIDDEN_HUB_ID_UPDATE_OPERATION(HttpStatus.FORBIDDEN, "4008", "허브 관리자는 허브 아이디를 수정할 수 없습니다."),
    FORBIDDEN_COMPANY_UPDATE_OPERATION(HttpStatus.FORBIDDEN, "4009",
        "업체담장자는 담당 업체에 등록된 상품만 수정할 수 있습니다."),
    FORBIDDEN_COMPANY_ID_UPDATE_OPERATION(HttpStatus.FORBIDDEN, "40010",
        "업체담당자는 업체 아이디를 수정할 수 없습니다."),
    FORBIDDEN_DELETE_HUB(HttpStatus.FORBIDDEN, "40011", "허브관리자는 담당 허브만 삭제할 수 있습니다."),
    HUB_NOT_FOUND(HttpStatus.NOT_FOUND, "40012", "존재하지 않는 허브입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
