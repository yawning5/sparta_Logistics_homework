package com.keepgoing.order.presentation.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.keepgoing.order.application.dto.CreateOrderCommand;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Builder
public record CreateOrderRequest(

    @NotNull(message = "공급 업체의 식별자는 필수값입니다.")
    @JsonProperty("supplier_id")
    UUID supplierId,

    @NotBlank(message = "공급 업체 이름은 필수값입니다.")
    @JsonProperty("supplier_name")
    String supplierName,

    @NotNull(message = "수령 업체 식별자는 필수값입니다.")
    @JsonProperty("receiver_id")
    UUID receiverId,

    @NotBlank(message = "수령 업체 이름은 필수값입니다.")
    @JsonProperty("receiver_name")
    String receiverName,

    @NotNull(message = "상품 식별자는 필수값입니다.")
    @JsonProperty("product_id")
    UUID productId,

    @NotBlank(message = "상품 이름은 필수값입니다.")
    @JsonProperty("product_name")
    String productName,

    @Positive(message = "상품 수량은 1개 이상의 자연수여야 합니다.")
    @Max(value = 1000, message = "상품 수량은 1000개를 초과할 수 없습니다.")
    @JsonProperty("quantity")
    Integer quantity,

    @PositiveOrZero(message = "가격은 0 이상의 정수여야 하며 음수는 사용할 수 없습니다.")
    @JsonProperty("price")
    Integer price,

    @NotNull(message = "납품 기한은 필수값입니다.")
    @JsonProperty("delivery_due_at")
    String deliveryDueAt,

    // 유효성 검증 필요 없음 - 값이 없을 수도 있기 때문
    @JsonProperty("delivery_request_note")
    String deliveryRequestNote
) {

    @Override
    public UUID supplierId() {
        return supplierId;
    }

    @Override
    public String supplierName() {
        return supplierName;
    }

    @Override
    public UUID receiverId() {
        return receiverId;
    }

    @Override
    public String receiverName() {
        return receiverName;
    }

    @Override
    public UUID productId() {
        return productId;
    }

    @Override
    public String productName() {
        return productName;
    }

    @Override
    public Integer quantity() {
        return quantity;
    }

    @Override
    public Integer price() {
        return price;
    }

    @Override
    public String deliveryDueAt() {
        return deliveryDueAt;
    }

    @Override
    public String deliveryRequestNote() {
        return deliveryRequestNote;
    }

    public CreateOrderCommand toCommand(Long memberId) {

        LocalDateTime time = LocalDateTime.parse(
            deliveryDueAt(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );
        return CreateOrderCommand.create(
            memberId,
            supplierId, supplierName, receiverId, receiverName, productId, productName,
            quantity, price, time, deliveryRequestNote
        );
    }
}
