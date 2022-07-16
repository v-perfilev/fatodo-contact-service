package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import com.persoff68.fatodo.model.dto.CreateContactEventDTO;
import com.persoff68.fatodo.model.dto.DeleteContactEventsDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventServiceClientWrapper implements EventServiceClient {

    @Qualifier("feignEventServiceClient")
    private final EventServiceClient eventServiceClient;

    @Override
    public void addContactEvent(CreateContactEventDTO createContactEventDTO) {
        try {
            eventServiceClient.addContactEvent(createContactEventDTO);
        } catch (Exception e) {
            throw new ClientException();
        }
    }

    @Override
    public void deleteContactEvents(DeleteContactEventsDTO deleteContactEventsDTO) {
        try {
            eventServiceClient.deleteContactEvents(deleteContactEventsDTO);
        } catch (Exception e) {
            throw new ClientException();
        }
    }
}
