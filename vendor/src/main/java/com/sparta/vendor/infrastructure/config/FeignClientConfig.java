package com.sparta.vendor.infrastructure.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.sparta.vendor.infrastructure.external.client")
public class FeignClientConfig {

}
