package com.example.springkafkatestcontainersdemo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

@SpringBootTest
@Import(TestSpringKafkaTestcontainersDemoApplication.class)
@EmbeddedKafka(partitions = 1, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
class SpringKafkaTestcontainersDemoApplicationTests {

    static final Logger log = LoggerFactory.getLogger(SpringKafkaTestcontainersDemoApplicationTests.class);

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ConsumerFactory<String, String> consumerFactory;

    @Test
    void send() throws Exception {
        try (var consumer = consumerFactory.createConsumer("test-send-kafka", null)) {
            //consumer.subscribe(List.of("topic1"));
            consumer.subscribe(Pattern.compile(".*"));
            log.info("Sending message...");
            log.info("Sent: {}", kafkaTemplate.send("topic1", "Hello world " + LocalDateTime.now()).get());
            log.info("Received: {}", KafkaTestUtils.getSingleRecord(consumer, "topic1"));
        }
    }

}
