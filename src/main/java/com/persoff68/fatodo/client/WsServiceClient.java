package com.persoff68.fatodo.client;

import com.persoff68.fatodo.client.configuration.FeignSystemConfiguration;
import com.persoff68.fatodo.model.dto.WsEventDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "ws-service", primary = false,
        configuration = {FeignSystemConfiguration.class},
        qualifiers = {"feignWsServiceClient"})
public interface WsServiceClient {

    @PostMapping(value = "/api/contact/request-incoming")
    void sendRequestIncomingEvent(@RequestBody WsEventDTO<UUID> event);

    @PostMapping(value = "/api/contact/request-outcoming")
    void sendRequestOutcomingEvent(@RequestBody WsEventDTO<UUID> event);

    @PostMapping(value = "/api/contact/accept-incoming")
    void sendAcceptIncomingEvent(@RequestBody WsEventDTO<UUID> event);

    @PostMapping(value = "/api/contact/accept-outcoming")
    void sendAcceptOutcomingEvent(@RequestBody WsEventDTO<UUID> event);

    @PostMapping(value = "/api/contact/delete-request-incoming")
    void sendDeleteRequestIncomingEvent(@RequestBody WsEventDTO<UUID> event);

    @PostMapping(value = "/api/contact/delete-request-outcoming")
    void sendDeleteRequestOutcomingEvent(@RequestBody WsEventDTO<UUID> event);

    @PostMapping(value = "/api/contact/delete-relation")
    void sendDeleteRelationEvent(@RequestBody WsEventDTO<UUID> event);

}

