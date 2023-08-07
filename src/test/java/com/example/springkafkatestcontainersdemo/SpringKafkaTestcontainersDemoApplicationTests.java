package com.example.springkafkatestcontainersdemo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;

@SpringBootTest
class SpringKafkaTestcontainersDemoApplicationTests {

    static final Logger log = LoggerFactory.getLogger(SpringKafkaTestcontainersDemoApplicationTests.class);

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void send() {
        log.info("Sending message...");
        kafkaTemplate.send("topic1", "Hello world " + LocalDateTime.now());
    }

}
