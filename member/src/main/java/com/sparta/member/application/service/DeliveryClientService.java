package com.sparta.member.application.service;

import com.sparta.member.interfaces.feign.DeliveryClient;
import com.sparta.member.interfaces.feign.dto.request.DeliveryManRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class DeliveryClientService {

    private final DeliveryClient deliveryClient;

    public void registerDeliveryMan(DeliveryManRequestDto requestDto, String token) {
            log.info("\n\nregisterDeliveryMan, token: {}\n\n", token);

            String bearerToken = token;

            deliveryClient.register(requestDto, token);
    }

}
