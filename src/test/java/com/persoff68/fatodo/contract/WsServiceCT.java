package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.builder.TestWsEventDTO;
import com.persoff68.fatodo.client.WsServiceClient;
import com.persoff68.fatodo.model.dto.WsEventDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
@AutoConfigureStubRunner(ids = {"com.persoff68.fatodo:wsservice:+:stubs"}, stubsMode =
        StubRunnerProperties.StubsMode.REMOTE)
class WsServiceCT {

    @Autowired
    WsServiceClient wsServiceClient;

    @Test
    void testSendRequestIncomingEvent() {
        WsEventDTO<UUID> dto = TestWsEventDTO.<UUID>defaultBuilder()
                .content(UUID.randomUUID()).build().toParent();
        assertDoesNotThrow(() -> wsServiceClient.sendRequestIncomingEvent(dto));
    }

    @Test
    void testSendRequestOutcomingEvent() {
        WsEventDTO<UUID> dto = TestWsEventDTO.<UUID>defaultBuilder()
                .content(UUID.randomUUID()).build().toParent();
        assertDoesNotThrow(() -> wsServiceClient.sendRequestOutcomingEvent(dto));
    }

    @Test
    void testSendAcceptIncomingEvent() {
        WsEventDTO<UUID> dto = TestWsEventDTO.<UUID>defaultBuilder()
                .content(UUID.randomUUID()).build().toParent();
        assertDoesNotThrow(() -> wsServiceClient.sendAcceptIncomingEvent(dto));
    }

    @Test
    void testSendAcceptOutcomingEvent() {
        WsEventDTO<UUID> dto = TestWsEventDTO.<UUID>defaultBuilder()
                .content(UUID.randomUUID()).build().toParent();
        assertDoesNotThrow(() -> wsServiceClient.sendAcceptOutcomingEvent(dto));
    }

    @Test
    void testSendDeleteRequestIncomingEvent() {
        WsEventDTO<UUID> dto = TestWsEventDTO.<UUID>defaultBuilder()
                .content(UUID.randomUUID()).build().toParent();
        assertDoesNotThrow(() -> wsServiceClient.sendDeleteRequestIncomingEvent(dto));
    }

    @Test
    void testSendDeleteRequestOutcomingEvent() {
        WsEventDTO<UUID> dto = TestWsEventDTO.<UUID>defaultBuilder()
                .content(UUID.randomUUID()).build().toParent();
        assertDoesNotThrow(() -> wsServiceClient.sendDeleteRequestOutcomingEvent(dto));
    }

    @Test
    void testSendDeleteRelationEvent() {
        WsEventDTO<UUID> dto = TestWsEventDTO.<UUID>defaultBuilder()
                .content(UUID.randomUUID()).build().toParent();
        assertDoesNotThrow(() -> wsServiceClient.sendDeleteRelationEvent(dto));
    }


}
