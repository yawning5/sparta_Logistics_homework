package com.keepgoing.order.config.external;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
@EnableKafka // 리스너를 찾아서 빈으로 등록
public class KafkaConfig {

    @Bean
    public NewTopic deliveryPrepareTopic() {
        return TopicBuilder.name("order.delivery")
            .partitions(3) // 해당 토픽에 대한 파티션 개수 지정
            .replicas(1) // 복제본 없이 단일 브로커에만 데이터 저장
            .build();
    }

    @Bean
    public NewTopic notificationPrepareTopic() {
        return TopicBuilder.name("order.notification")
            .partitions(3) // 해당 토픽에 대한 파티션 개수 지정
            .replicas(1) // 복제본 없이 단일 브로커에만 데이터 저장
            .build();
    }
}
