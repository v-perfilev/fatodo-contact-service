package com.persoff68.fatodo.client;

import com.persoff68.fatodo.client.configuration.FeignAuthConfiguration;
import com.persoff68.fatodo.model.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "chat-service", primary = false,
        configuration = {FeignAuthConfiguration.class},
        qualifiers = {"feignChatServiceClient"})
public interface ChatServiceClient {

    @PostMapping(value = "/api/message/{recipientId}/direct")
    void sendDirect(@PathVariable UUID recipientId, @RequestBody Message message);

}

