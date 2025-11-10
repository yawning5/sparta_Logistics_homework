package com.keepgoing.order.config.inner;

import java.time.Clock;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TimeConfog {

    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

}
