package com.keepgoing.order.presentation.api;

import com.keepgoing.order.domain.order.PaymentApplyResult;
import com.keepgoing.order.jwt.CustomPrincipal;
import com.keepgoing.order.presentation.dto.response.api.CancelOrderResponse;
import com.keepgoing.order.presentation.dto.response.api.DeleteOrderInfo;
import com.keepgoing.order.presentation.dto.response.api.OrderInfo;
import com.keepgoing.order.presentation.dto.request.CreateOrderRequest;
import com.keepgoing.order.presentation.dto.response.base.BaseResponseDto;
import com.keepgoing.order.presentation.dto.response.api.CreateOrderResponse;
import com.keepgoing.order.presentation.dto.response.api.OrderStateInfo;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

public interface OrderController {

    BaseResponseDto<CreateOrderResponse> create(CreateOrderRequest request, CustomPrincipal customPrincipal);

    BaseResponseDto<Page<OrderInfo>> getOrderInfoList(Pageable pageable);

    BaseResponseDto<OrderInfo> getOrderInfoList(UUID orderId);

    BaseResponseDto<OrderStateInfo> getOrderState(UUID orderId);

    BaseResponseDto<PaymentApplyResult> updateStateToPaid(@PathVariable UUID orderId);

    BaseResponseDto<DeleteOrderInfo> deleteOrder(UUID orderId, CustomPrincipal customPrincipal);

    BaseResponseDto<CancelOrderResponse> cancel(UUID orderId, CustomPrincipal customPrincipal);
}
