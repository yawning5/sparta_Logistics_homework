package com.keepgoing.order.presentation.api;

import com.keepgoing.order.application.service.order.OrderService;
import com.keepgoing.order.domain.order.PaymentApplyResult;
import com.keepgoing.order.jwt.CustomPrincipal;
import com.keepgoing.order.presentation.dto.request.CreateOrderRequest;
import com.keepgoing.order.presentation.dto.response.api.CancelOrderResponse;
import com.keepgoing.order.presentation.dto.response.base.BaseResponseDto;
import com.keepgoing.order.presentation.dto.response.api.CreateOrderResponse;
import com.keepgoing.order.presentation.dto.response.api.DeleteOrderInfo;
import com.keepgoing.order.presentation.dto.response.api.OrderInfo;
import com.keepgoing.order.presentation.dto.response.api.OrderStateInfo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.SortDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV1 implements OrderController{

    private final OrderService orderService;

    @Override
    @PostMapping("/v1/orders")
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB', 'DELIVERY', 'COMPANY')")
    public BaseResponseDto<CreateOrderResponse> create(@Valid @RequestBody CreateOrderRequest request, @AuthenticationPrincipal
        CustomPrincipal customPrincipal) {

        Long memberId = customPrincipal.userId();
        CreateOrderResponse response = orderService.create(request.toCommand(memberId));

        return BaseResponseDto.success(response);
    }

    @Override
    @GetMapping("/v1/orders")
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB', 'DELIVERY', 'COMPANY')")
    public BaseResponseDto<Page<OrderInfo>> getOrderInfoList(
        @SortDefault.SortDefaults({
            @SortDefault(sort = "createdAt", direction = Sort.Direction.DESC),
            @SortDefault(sort = "updatedAt", direction = Sort.Direction.DESC)
        })
        Pageable pageable
    ) {

        validate(pageable);

        Page<OrderInfo> searchOrderPage = orderService.getOrderPage(pageable);
        return BaseResponseDto.success(searchOrderPage);
    }

    @Override
    @GetMapping("/v1/orders/{orderId}")
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB', 'DELIVERY', 'COMPANY')")
    public BaseResponseDto<OrderInfo> getOrderInfoList(@PathVariable @NotNull UUID orderId) {
        return BaseResponseDto.success(orderService.searchOrderOne(orderId));
    }

    private void validate(Pageable pageable) {
        int page = pageable.getPageNumber();
        int size = pageable.getPageSize();

        if (page < 0) {
            throw new IllegalArgumentException("page는 0 이상이어야 합니다.");
        }

        if (size <= 0 || size > 100) {
            throw new IllegalArgumentException("size는 1 ~ 100 사이여야 합니다.");
        }
    }

    @Override
    @GetMapping("/v1/orders/{orderId}/status")
    public BaseResponseDto<OrderStateInfo> getOrderState(@PathVariable @NotNull UUID orderId) {
        OrderStateInfo orderStateInfo = orderService.findOrderState(orderId);
        return BaseResponseDto.success(orderStateInfo);
    }

    @Override
    @PatchMapping("/v1/orders/{orderId}/confirm-payment")
    public BaseResponseDto<PaymentApplyResult> updateStateToPaid(@PathVariable @NotNull UUID orderId) {
        PaymentApplyResult result = orderService.updateStateToPaid(orderId);
        return BaseResponseDto.success(result);
    }

    @Override
    @DeleteMapping("/v1/orders/{orderId}")
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB')")
    public BaseResponseDto<DeleteOrderInfo> deleteOrder(@PathVariable @NotNull UUID orderId, @AuthenticationPrincipal CustomPrincipal customPrincipal) {

        Long memberId = customPrincipal.userId();
        DeleteOrderInfo deleteOrderInfo = orderService.deleteOrder(orderId, memberId);

        return BaseResponseDto.success(deleteOrderInfo);
    }

    @Override
    @PostMapping("/v1/orders/{orderId}/cancel")
    @PreAuthorize("hasAnyAuthority('MASTER', 'HUB', 'DELIVERY', 'COMPANY')")
    public BaseResponseDto<CancelOrderResponse> cancel(@PathVariable UUID orderId, @AuthenticationPrincipal CustomPrincipal customPrincipal) {

        Long memberId = customPrincipal.userId();
        CancelOrderResponse response = orderService.updateCancelStateCancelRequired(orderId, memberId);
        return BaseResponseDto.success(response);
    }
}
