package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import com.persoff68.fatodo.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ChatServiceClientWrapper implements ChatServiceClient {

    @Qualifier("feignChatServiceClient")
    private final ChatServiceClient chatServiceClient;

    @Override
    public void sendDirect(UUID recipientId, Message message) {
        try {
            chatServiceClient.sendDirect(recipientId, message);
        } catch (Exception e) {
            throw new ClientException();
        }
    }
}
