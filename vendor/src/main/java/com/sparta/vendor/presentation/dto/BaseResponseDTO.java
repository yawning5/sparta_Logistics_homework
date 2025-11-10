package com.sparta.vendor.presentation.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import com.sparta.vendor.application.exception.ErrorCode;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record BaseResponseDTO<T>(
    boolean success,
    T data,
    Error error
) {

    // ⚡ Error record
    public record Error(String code, String message) {

    }

    // ✅ 성공 응답 생성 (데이터 없는 경우)
    public static <T> BaseResponseDTO<T> ok() {
        return new BaseResponseDTO<>(true, null, null);
    }

    // ✅ 성공 응답 생성 (데이터 있는 경우)
    public static <T> BaseResponseDTO<T> success(T data) {
        return new BaseResponseDTO<>(true, data, null);
    }

    // ✅ 실패 응답 생성 (ErrorCode 사용)
    public static <T> BaseResponseDTO<T> error(ErrorCode errorCode) {
        return new BaseResponseDTO<>(
            false,
            null,
            new Error(errorCode.getCode(), errorCode.getMessage())
        );
    }

    // ✅ 실패 응답 생성 (커스텀 메시지)
    public static <T> BaseResponseDTO<T> error(String code, String message) {
        return new BaseResponseDTO<>(
            false,
            null,
            new Error(code, message)
        );
    }
}
