package com.keepgoing.order.presentation.api;

import com.keepgoing.order.domain.order.OrderState;
import com.keepgoing.order.presentation.dto.response.OrderInfo;
import com.keepgoing.order.presentation.dto.request.CreateOrderRequest;
import com.keepgoing.order.presentation.dto.response.BaseResponseDto;
import com.keepgoing.order.presentation.dto.response.CreateOrderResponse;
import com.keepgoing.order.presentation.dto.response.OrderStateInfo;
import com.keepgoing.order.presentation.dto.response.UpdateOrderStateInfo;
import java.awt.PageAttributes;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PathVariable;

public interface OrderController {

    BaseResponseDto<CreateOrderResponse> create(CreateOrderRequest request);

    BaseResponseDto<Page<OrderInfo>> getOrderInfoList(Pageable pageable);

    BaseResponseDto<OrderInfo> getOrderInfoList(UUID orderId);

    BaseResponseDto<OrderStateInfo> getOrderState(UUID orderId);

    BaseResponseDto<UpdateOrderStateInfo> updateStateToPaid(@PathVariable UUID orderId);
}
