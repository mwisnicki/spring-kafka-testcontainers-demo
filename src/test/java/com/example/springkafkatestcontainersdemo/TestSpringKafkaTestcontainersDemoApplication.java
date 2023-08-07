package com.example.springkafkatestcontainersdemo;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
public class TestSpringKafkaTestcontainersDemoApplication {

    static final Logger log = LoggerFactory.getLogger(TestSpringKafkaTestcontainersDemoApplication.class);

    @Bean
    @ServiceConnection
    KafkaContainer kafkaContainer() {
        return new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:latest"));
    }

    @KafkaListener(topics = "topic1", groupId = "test-log")
    void onMessage(ConsumerRecord<String, String> record) {
        log.error("Message received: {}", record);
    }

    public static void main(String[] args) {
        SpringApplication.from(SpringKafkaTestcontainersDemoApplication::main).with(TestSpringKafkaTestcontainersDemoApplication.class).run(args);
    }

}
