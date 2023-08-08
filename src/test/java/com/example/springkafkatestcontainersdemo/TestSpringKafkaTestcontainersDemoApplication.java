package com.example.springkafkatestcontainersdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration(proxyBeanMethods = false)
@Import(KafkaLogger.class)
public class TestSpringKafkaTestcontainersDemoApplication {

//    @Bean
//    @ServiceConnection
//    KafkaContainer kafkaContainer() {
//        return new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
//    }

    public static void main(String[] args) {
        SpringApplication.from(SpringKafkaTestcontainersDemoApplication::main).with(TestSpringKafkaTestcontainersDemoApplication.class).run(args);
    }

}
