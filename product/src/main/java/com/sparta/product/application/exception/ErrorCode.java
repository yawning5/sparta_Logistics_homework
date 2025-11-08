package com.sparta.product.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "000", "서버 내부 오류입니다."),
    FORBIDDEN_HUB_OPERATION(HttpStatus.FORBIDDEN, "4001", "허브 관리자는 담당 허브에만 상품을 등록할 수 있습니다."),
    FORBIDDEN_COMPANY_OPERATION(HttpStatus.FORBIDDEN, "4002", "업체 담당자는 담당 업체에만 상품을 등록할 수 있습니다."),
    VENDOR_NOT_FOUND(HttpStatus.NOT_FOUND, "4003", "존재하지 않는 업체 입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
