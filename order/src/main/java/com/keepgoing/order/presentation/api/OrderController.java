package com.keepgoing.order.presentation.api;

import com.keepgoing.order.presentation.dto.request.CreateOrderRequest;
import com.keepgoing.order.presentation.dto.response.BaseResponseDto;
import com.keepgoing.order.presentation.dto.response.CreateOrderResponse;

public interface OrderController {

    BaseResponseDto<CreateOrderResponse> create(CreateOrderRequest request);

}
