package com.persoff68.fatodo.web.kafka;

import com.persoff68.fatodo.client.WsServiceClient;
import com.persoff68.fatodo.config.annotation.ConditionalOnPropertyNotNull;
import com.persoff68.fatodo.config.constant.KafkaTopics;
import com.persoff68.fatodo.model.dto.WsEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@ConditionalOnPropertyNotNull(value = "kafka.bootstrapAddress")
public class WsProducer implements WsServiceClient {

    private final KafkaTemplate<String, WsEventDTO<UUID>> wsContactEventKafkaTemplate;

    public void sendRequestIncomingEvent(WsEventDTO<UUID> event) {
        wsContactEventKafkaTemplate.send(KafkaTopics.WS_CONTACT.getValue(), "request-incoming", event);
    }

    public void sendRequestOutcomingEvent(WsEventDTO<UUID> event) {
        wsContactEventKafkaTemplate.send(KafkaTopics.WS_CONTACT.getValue(), "request-outcoming", event);
    }

    public void sendAcceptIncomingEvent(WsEventDTO<UUID> event) {
        wsContactEventKafkaTemplate.send(KafkaTopics.WS_CONTACT.getValue(), "accept-incoming", event);
    }

    public void sendAcceptOutcomingEvent(WsEventDTO<UUID> event) {
        wsContactEventKafkaTemplate.send(KafkaTopics.WS_CONTACT.getValue(), "accept-outcoming", event);
    }

    @Override
    public void sendDeleteRequestIncomingEvent(WsEventDTO<UUID> event) {
        wsContactEventKafkaTemplate.send(KafkaTopics.WS_CONTACT.getValue(), "delete-request-incoming", event);
    }

    @Override
    public void sendDeleteRequestOutcomingEvent(WsEventDTO<UUID> event) {
        wsContactEventKafkaTemplate.send(KafkaTopics.WS_CONTACT.getValue(), "delete-request-outcoming", event);
    }

    @Override
    public void sendDeleteRelationEvent(WsEventDTO<UUID> event) {
        wsContactEventKafkaTemplate.send(KafkaTopics.WS_CONTACT.getValue(), "delete-relation", event);
    }

}
