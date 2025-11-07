package com.sparta.vendor.application.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;


@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "000", "서버 내부 오류입니다."),
    FORBIDDEN_HUB_OPERATION(HttpStatus.FORBIDDEN, "8001", "허브 관리자는 담당 허브에만 업체를 등록할 수 있습니다."),
    VENDOR_NOT_FOUND(HttpStatus.NOT_FOUND, "8002", "헤당 아이디의 업체를 찾을 수 없습니다."),
    VENDOR_DELETED(HttpStatus.GONE, "8003", "삭제된 업체 입니다."),
    FORBIDDEN_HUB_UPDATE_OPERATION(HttpStatus.FORBIDDEN, "8004", "허브관리자는 담당 허브에만 업체를 수정할 수 있습니다."),
    FORBIDDEN_COMPONY_UPDATE_OPERATION(HttpStatus.FORBIDDEN, "8005", "업체담당자는 자신의 업체만 수정할 수 있습니다."),
    FORBIDDEN_HUB_ID_MODIFICATION(HttpStatus.FORBIDDEN, "8006", "허브관리자는 허브아이디를 수정할 수 없습니다."),
    FORBIDDEN_COMPONY_ID_MODIFICATION(HttpStatus.FORBIDDEN, "8007", "업체 담당자는 허브아이디를 수정할 수 없습니다."),
    FORBIDDEN_DELETE_HUB(HttpStatus.FORBIDDEN, "8008", "허브관리자는 담당 허브만 삭제할 수 있습니다.");


    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
