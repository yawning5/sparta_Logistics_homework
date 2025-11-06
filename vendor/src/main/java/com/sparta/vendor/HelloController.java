package com.sparta.vendor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/v1/stores/hello")
    public String hello() {
        System.out.println("들어옴!");
        return "Hello World!!!";
    }
}
