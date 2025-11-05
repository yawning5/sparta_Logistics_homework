package com.keepgoing.order;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/v1/orders/test")
    public String test() {
        return "test";
    }
}
