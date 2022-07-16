package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.client.EventServiceClient;
import com.persoff68.fatodo.model.dto.CreateContactEventDTO;
import com.persoff68.fatodo.model.dto.DeleteContactEventsDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@AutoConfigureStubRunner(ids = {"com.persoff68.fatodo:eventservice:+:stubs"},
        stubsMode = StubRunnerProperties.StubsMode.REMOTE)
class EventServiceCT {

    @Autowired
    EventServiceClient eventServiceClient;

    @Test
    void testAddContactEvent() {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        CreateContactEventDTO dto = CreateContactEventDTO.contactAccept(userId1, userId2);
        assertDoesNotThrow(() -> eventServiceClient.addContactEvent(dto));
    }

    @Test
    void testDeleteContactEventsForUser() {
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        DeleteContactEventsDTO dto = new DeleteContactEventsDTO(userId1, userId2);
        assertDoesNotThrow(() -> eventServiceClient.deleteContactEvents(dto));
    }

}
