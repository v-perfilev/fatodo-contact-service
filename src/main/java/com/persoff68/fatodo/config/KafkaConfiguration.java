package com.persoff68.fatodo.config;

import com.persoff68.fatodo.config.annotation.ConditionalOnPropertyNotNull;
import com.persoff68.fatodo.config.constant.KafkaTopics;
import com.persoff68.fatodo.config.util.KafkaUtils;
import com.persoff68.fatodo.model.dto.ClearEventDTO;
import com.persoff68.fatodo.model.dto.CreateContactEventDTO;
import com.persoff68.fatodo.model.dto.DeleteContactEventsDTO;
import com.persoff68.fatodo.model.dto.RequestEventDTO;
import com.persoff68.fatodo.model.dto.WsEventDTO;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableKafka
@ConditionalOnPropertyNotNull(value = "kafka.bootstrapAddress")
public class KafkaConfiguration {

    @Value(value = "${kafka.bootstrapAddress}")
    private String bootstrapAddress;

    @Value(value = "${kafka.partitions}")
    private int partitions;

    @Bean
    public KafkaAdmin kafkaAdmin() {
        return KafkaUtils.buildKafkaAdmin(bootstrapAddress);
    }

    @Bean
    public NewTopic eventAddNewTopic() {
        return KafkaUtils.buildTopic(KafkaTopics.EVENT_ADD.getValue(), partitions);
    }

    @Bean
    public NewTopic eventDeleteNewTopic() {
        return KafkaUtils.buildTopic(KafkaTopics.EVENT_DELETE.getValue(), partitions);
    }

    @Bean
    public NewTopic wsClearNewTopic() {
        return KafkaUtils.buildTopic(KafkaTopics.WS_CLEAR.getValue(), partitions);
    }

    @Bean
    public NewTopic wsContactNewTopic() {
        return KafkaUtils.buildTopic(KafkaTopics.WS_CONTACT.getValue(), partitions);
    }

    @Bean
    public KafkaTemplate<String, CreateContactEventDTO> eventContactKafkaTemplate() {
        return KafkaUtils.buildJsonKafkaTemplate(bootstrapAddress);
    }

    @Bean
    public KafkaTemplate<String, DeleteContactEventsDTO> eventDeleteContactKafkaTemplate() {
        return KafkaUtils.buildJsonKafkaTemplate(bootstrapAddress);
    }

    @Bean
    public KafkaTemplate<String, WsEventDTO<ClearEventDTO>> wsClearEventKafkaTemplate() {
        return KafkaUtils.buildJsonKafkaTemplate(bootstrapAddress);
    }

    @Bean
    public KafkaTemplate<String, WsEventDTO<RequestEventDTO>> wsContactEventKafkaTemplate() {
        return KafkaUtils.buildJsonKafkaTemplate(bootstrapAddress);
    }

}
