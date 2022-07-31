package com.persoff68.fatodo.web.kafka;

import com.persoff68.fatodo.client.WsServiceClient;
import com.persoff68.fatodo.config.annotation.ConditionalOnPropertyNotNull;
import com.persoff68.fatodo.config.constant.KafkaTopics;
import com.persoff68.fatodo.model.dto.ClearEventDTO;
import com.persoff68.fatodo.model.dto.RequestEventDTO;
import com.persoff68.fatodo.model.dto.WsEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnPropertyNotNull(value = "kafka.bootstrapAddress")
public class WsProducer implements WsServiceClient {

    private final KafkaTemplate<String, WsEventDTO<RequestEventDTO>> wsContactEventKafkaTemplate;
    private final KafkaTemplate<String, WsEventDTO<ClearEventDTO>> wsClearEventKafkaTemplate;

    public void sendRequestIncomingEvent(WsEventDTO<RequestEventDTO> event) {
        wsContactEventKafkaTemplate.send(KafkaTopics.WS_CONTACT.getValue(), "request-incoming", event);
    }

    public void sendRequestOutcomingEvent(WsEventDTO<RequestEventDTO> event) {
        wsContactEventKafkaTemplate.send(KafkaTopics.WS_CONTACT.getValue(), "request-outcoming", event);
    }

    public void sendAcceptIncomingEvent(WsEventDTO<RequestEventDTO> event) {
        wsContactEventKafkaTemplate.send(KafkaTopics.WS_CONTACT.getValue(), "accept-incoming", event);
    }

    public void sendAcceptOutcomingEvent(WsEventDTO<RequestEventDTO> event) {
        wsContactEventKafkaTemplate.send(KafkaTopics.WS_CONTACT.getValue(), "accept-outcoming", event);
    }

    public void sendClearEvent(WsEventDTO<ClearEventDTO> event) {
        wsClearEventKafkaTemplate.send(KafkaTopics.WS_CLEAR.getValue(), event);
    }

}
