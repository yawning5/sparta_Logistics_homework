package com.keepgoing.order.presentation.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponseDto<T> {

    private boolean success; // true / false
    private T data;          // 성공 시 데이터
    private Error error;     // 실패 시 에러 정보

    @Getter
    @AllArgsConstructor
    @Builder
    public static class Error {
        private String code;
        private String message;
    }

    // ✅ 성공 응답
    public static <T> BaseResponseDto<T> success(T data) {
        return BaseResponseDto.<T>builder()
            .success(true)
            .data(data)
            .build();
    }

    // ✅ 실패 응답 (ErrorCode 사용)
    public static <T> BaseResponseDto<T> error(ErrorCode errorCode) {
        return BaseResponseDto.<T>builder()
            .success(false)
            .error(Error.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build())
            .build();
    }

    // ✅ 실패 응답 (커스텀 메시지)
    public static <T> BaseResponseDto<T> error(String code, String message) {
        return BaseResponseDto.<T>builder()
            .success(false)
            .error(Error.builder()
                .code(code)
                .message(message)
                .build())
            .build();
    }
}
