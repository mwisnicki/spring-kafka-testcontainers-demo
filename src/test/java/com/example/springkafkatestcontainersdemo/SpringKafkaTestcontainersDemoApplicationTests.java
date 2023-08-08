package com.example.springkafkatestcontainersdemo;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
import java.util.Properties;
import java.util.regex.Pattern;

@SpringBootTest
@Import(TestSpringKafkaTestcontainersDemoApplication.class)
@EmbeddedKafka(partitions = 1, bootstrapServersProperty = "spring.kafka.bootstrap-servers")
@ExtendWith(TestConsumerParameterResolver.class)
class SpringKafkaTestcontainersDemoApplicationTests {

    static final Logger log = LoggerFactory.getLogger(SpringKafkaTestcontainersDemoApplicationTests.class);

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Test
    void send_and_receive(Consumer<String, String> consumer) throws Exception {
        consumer.subscribe(Pattern.compile(".*"));
        log.info("Sending message...");
        log.info("Sent: {}", kafkaTemplate.send("topic1", "Hello world " + LocalDateTime.now()).get());
        log.info("Received: {}", KafkaTestUtils.getSingleRecord(consumer, "topic1"));
    }

}
