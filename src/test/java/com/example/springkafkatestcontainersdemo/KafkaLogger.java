package com.example.springkafkatestcontainersdemo;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Lazy;
import org.springframework.kafka.annotation.KafkaListener;

/**
 * Logs all kafka messages.
 */
@TestConfiguration
@Lazy(value = false) // required if using spring lazy init
public class KafkaLogger {
    private final static Logger log = LoggerFactory.getLogger("kafka.records");

    @KafkaListener(topicPattern = ".*", groupId = "kafka-logger", properties = {
            // required when topic is created on send, otherwise consumers start after send and won't pick up earlier messages
            ConsumerConfig.AUTO_OFFSET_RESET_CONFIG + "=earliest",
            // required for topicPattern, otherwise new topics are only picked up too late
            ConsumerConfig.METADATA_MAX_AGE_CONFIG + "=100"
    })
    void onMessage(ConsumerRecord<?, ?> record) {
        LoggerFactory.getLogger("kafka.messages." + record.topic()).info("{}", record.value());
        log.info("{}", record);
    }
}
