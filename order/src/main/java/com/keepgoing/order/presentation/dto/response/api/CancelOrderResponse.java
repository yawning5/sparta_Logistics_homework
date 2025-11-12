package com.keepgoing.order.presentation.dto.response.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
public class CancelOrderResponse {

    @JsonProperty("order_id")
    private UUID orderId;

    @JsonProperty("cancel_requested")
    private boolean cancelRequested;

    @JsonProperty("member_id")
    private Long memberId;

    @JsonProperty("cancel_required_at")
    private String cancelRequiredAt;

    @JsonProperty("message")
    private String message;


    @Builder
    private CancelOrderResponse(UUID orderId, boolean cancelRequested, Long memberId, LocalDateTime cancelRequiredAt, String message) {
        this.orderId = orderId;
        this.cancelRequested = cancelRequested;
        this.memberId = memberId;
        this.cancelRequiredAt = String.valueOf(cancelRequiredAt);
        this.message = message;
    }

    public static CancelOrderResponse success(UUID orderId, Long memberId, LocalDateTime cancelRequiredAt, String message) {
        return CancelOrderResponse.builder()
            .orderId(orderId)
            .cancelRequested(true)
            .memberId(memberId)
            .cancelRequiredAt(cancelRequiredAt)
            .message(message)
            .build();
    }

    public static CancelOrderResponse fail(UUID orderId, Long memberId, LocalDateTime cancelRequiredAt, String message) {
        return CancelOrderResponse.builder()
            .orderId(orderId)
            .cancelRequested(false)
            .memberId(memberId)
            .cancelRequiredAt(cancelRequiredAt)
            .message(message)
            .build();
    }

}
