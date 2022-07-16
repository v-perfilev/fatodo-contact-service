package com.persoff68.fatodo.web.kafka;

import com.persoff68.fatodo.client.EventServiceClient;
import com.persoff68.fatodo.config.annotation.ConditionalOnPropertyNotNull;
import com.persoff68.fatodo.config.constant.KafkaTopics;
import com.persoff68.fatodo.model.dto.CreateContactEventDTO;
import com.persoff68.fatodo.model.dto.DeleteContactEventsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@ConditionalOnPropertyNotNull(value = "kafka.bootstrapAddress")
public class EventProducer implements EventServiceClient {

    private final KafkaTemplate<String, CreateContactEventDTO> eventContactKafkaTemplate;
    private final KafkaTemplate<String, DeleteContactEventsDTO> eventDeleteContactKafkaTemplate;

    @Override
    public void addContactEvent(CreateContactEventDTO createContactEventDTO) {
        eventContactKafkaTemplate.send(KafkaTopics.EVENT_ADD.getValue(), "contact", createContactEventDTO);
    }

    @Override
    public void deleteContactEvents(DeleteContactEventsDTO deleteContactEventsDTO) {
        eventDeleteContactKafkaTemplate.send(KafkaTopics.EVENT_DELETE.getValue(), "contact-delete",
                deleteContactEventsDTO);
    }
}
