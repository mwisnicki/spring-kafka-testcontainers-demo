package com.example.springkafkatestcontainersdemo;

import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.common.TopicPartition;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

@SpringBootTest
@Import(TestSpringKafkaTestcontainersDemoApplication.class)
class SpringKafkaTestcontainersDemoApplicationTests {

    static final Logger log = LoggerFactory.getLogger(SpringKafkaTestcontainersDemoApplicationTests.class);

    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    ConsumerFactory<String, String> consumerFactory;

    @Test
    void send() throws Exception {
        try (var consumer = consumerFactory.createConsumer("test-send-kafka", null)) {
            consumer.subscribe(List.of("topic1"));
            // does not work at all
            //consumer.subscribe(Pattern.compile(".*"));
            log.info("Init...");
            // if next line is commented or duration reduced to less than 1s then receive at the end will fail
            log.info("Records={}", IterableUtil.toCollection(KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(1))));
            log.info("Sending message...");
            log.info("Sent: {}", kafkaTemplate.send("topic1", "Hello world " + LocalDateTime.now()).get());
            log.info("Received: {}", KafkaTestUtils.getSingleRecord(consumer, "topic1"));
        }
    }

}
