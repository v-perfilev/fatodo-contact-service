package com.persoff68.fatodo.client;

import com.persoff68.fatodo.client.configuration.FeignSystemConfiguration;
import com.persoff68.fatodo.model.dto.CreateContactEventDTO;
import com.persoff68.fatodo.model.dto.DeleteContactEventsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "event-service", primary = false,
        configuration = {FeignSystemConfiguration.class},
        qualifiers = {"feignEventServiceClient"})
public interface EventServiceClient {

    @PostMapping(value = "/api/event/contact")
    void addContactEvent(@RequestBody CreateContactEventDTO createContactEventDTO);

    @PostMapping(value = "/api/event/contact/delete")
    void deleteContactEvents(@RequestBody DeleteContactEventsDTO deleteContactEventsDTO);

}
