package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.client.ChatServiceClient;
import com.persoff68.fatodo.model.Message;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;

@SpringBootTest
@AutoConfigureStubRunner(ids = {"com.persoff68.fatodo:chatservice:+:stubs"},
        stubsMode = StubRunnerProperties.StubsMode.REMOTE)
class ChatServiceCT {

    @Autowired
    ChatServiceClient chatServiceClient;

    @Test
    void sendDirect() {
        Message message = new Message();
        message.setText("test");
        assertThatCode(() -> chatServiceClient.sendDirect(UUID.randomUUID(), message)).doesNotThrowAnyException();
    }

}
