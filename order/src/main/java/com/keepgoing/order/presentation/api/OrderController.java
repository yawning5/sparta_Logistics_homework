package com.keepgoing.order.presentation.api;

import com.keepgoing.order.presentation.dto.response.OrderInfo;
import com.keepgoing.order.presentation.dto.request.CreateOrderRequest;
import com.keepgoing.order.presentation.dto.response.BaseResponseDto;
import com.keepgoing.order.presentation.dto.response.CreateOrderResponse;
import java.awt.PageAttributes;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderController {

    BaseResponseDto<CreateOrderResponse> create(CreateOrderRequest request);

    BaseResponseDto<Page<OrderInfo>> getOrderInfoList(Pageable pageable);
}
