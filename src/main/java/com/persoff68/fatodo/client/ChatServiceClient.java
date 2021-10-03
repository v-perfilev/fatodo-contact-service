package com.persoff68.fatodo.client;

import com.persoff68.fatodo.model.Message;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.UUID;

@FeignClient(name = "chat-service", primary = false)
public interface ChatServiceClient {

    @PostMapping(value = "/api/messages/direct/{recipientId}")
    void sendDirect(@PathVariable UUID recipientId, @RequestBody Message message);

}

