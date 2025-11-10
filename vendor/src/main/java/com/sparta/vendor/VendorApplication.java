package com.keepgoing.vendor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
public class VendorApplication {

    public static void main(String[] args) {
        SpringApplication.run(VendorApplication.class, args);
    }

}
