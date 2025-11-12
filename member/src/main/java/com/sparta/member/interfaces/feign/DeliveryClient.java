package com.sparta.member.interfaces.feign;

import com.sparta.member.interfaces.feign.dto.request.DeliveryManRequestDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "delivery-service")
public interface DeliveryClient {

    @PostMapping("/v1/delivery-persons")
    void register(@RequestBody DeliveryManRequestDto deliveryManRequestDto,
        @RequestHeader("Authorization") String token);
}
