package com.persoff68.fatodo.service.client;

import com.persoff68.fatodo.client.EventServiceClient;
import com.persoff68.fatodo.model.dto.CreateContactEventDTO;
import com.persoff68.fatodo.model.dto.DeleteContactEventsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Async
public class EventService {

    private final EventServiceClient eventServiceClient;

    public void sendContactSendEvent(UUID firstUserId, UUID secondUserId) {
        CreateContactEventDTO dto = CreateContactEventDTO.contactSend(firstUserId, secondUserId);
        eventServiceClient.addContactEvent(dto);
    }

    public void sendContactAcceptEvent(UUID firstUserId, UUID secondUserId) {
        CreateContactEventDTO dto = CreateContactEventDTO.contactAccept(firstUserId, secondUserId);
        eventServiceClient.addContactEvent(dto);
    }

    public void deleteContactEventsForUserEvents(UUID firstUserId, UUID secondUserId) {
        DeleteContactEventsDTO dto = new DeleteContactEventsDTO(firstUserId, secondUserId);
        eventServiceClient.deleteContactEvents(dto);
    }

}
