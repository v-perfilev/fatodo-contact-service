package com.persoff68.fatodo.client;

import com.persoff68.fatodo.client.configuration.FeignAuthConfiguration;
import com.persoff68.fatodo.client.configuration.FeignSystemConfiguration;
import com.persoff68.fatodo.model.dto.CreateContactEventDTO;
import com.persoff68.fatodo.model.dto.DeleteContactEventsDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "event-service", primary = false,
        configuration = {FeignSystemConfiguration.class},
        qualifiers = {"feignEventServiceClient"})
public interface EventServiceClient {

    @PostMapping(value = "/api/events/contact", consumes = MediaType.APPLICATION_JSON_VALUE)
    void addContactEvent(@RequestBody CreateContactEventDTO createContactEventDTO);

    @PostMapping(value = "/api/events/contact/delete", consumes = MediaType.APPLICATION_JSON_VALUE)
    void deleteContactEvents(@RequestBody DeleteContactEventsDTO deleteContactEventsDTO);

}
