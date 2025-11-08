package com.keepgoing.order.presentation.api;

import com.keepgoing.order.application.service.order.OrderService;
import com.keepgoing.order.presentation.dto.request.CreateOrderRequest;
import com.keepgoing.order.presentation.dto.response.BaseResponseDto;
import com.keepgoing.order.presentation.dto.response.CreateOrderResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderControllerV1 implements OrderController{

    private final OrderService orderService;

    @Override
    @PostMapping("/v1/orders")
    public BaseResponseDto<CreateOrderResponse> create(@Valid @RequestBody CreateOrderRequest request) {
        CreateOrderResponse response = orderService.create(request.toCommand());
        return BaseResponseDto.success(response);
    }
}
