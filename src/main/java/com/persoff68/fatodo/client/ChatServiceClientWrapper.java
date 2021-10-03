package com.persoff68.fatodo.client;

import com.persoff68.fatodo.exception.ClientException;
import com.persoff68.fatodo.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Primary
@RequiredArgsConstructor
public class ChatServiceClientWrapper implements ChatServiceClient {

    @Qualifier("chatServiceClient")
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
