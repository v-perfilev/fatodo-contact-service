package com.persoff68.fatodo.service.client;

import com.persoff68.fatodo.client.WsServiceClient;
import com.persoff68.fatodo.model.dto.WsEventDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WsService {

    private final WsServiceClient wsServiceClient;

    public void sendRequestIncomingEvent(UUID requesterId, UUID recipientId) {
        List<UUID> userIdList = Collections.singletonList(recipientId);
        WsEventDTO<UUID> eventDTO = new WsEventDTO<>(userIdList, requesterId);
        sendRequestIncomingEventAsync(eventDTO);
    }

    public void sendRequestOutcomingEvent(UUID requesterId, UUID recipientId) {
        List<UUID> userIdList = Collections.singletonList(requesterId);
        WsEventDTO<UUID> eventDTO = new WsEventDTO<>(userIdList, recipientId);
        sendRequestOutcomingEventAsync(eventDTO);
    }

    public void sendAcceptIncomingEvent(UUID requesterId, UUID recipientId) {
        List<UUID> userIdList = Collections.singletonList(recipientId);
        WsEventDTO<UUID> eventDTO = new WsEventDTO<>(userIdList, requesterId);
        sendAcceptIncomingEventAsync(eventDTO);
    }

    public void sendAcceptOutcomingEvent(UUID requesterId, UUID recipientId) {
        List<UUID> userIdList = Collections.singletonList(requesterId);
        WsEventDTO<UUID> eventDTO = new WsEventDTO<>(userIdList, recipientId);
        sendAcceptOutcomingEventAsync(eventDTO);
    }

    public void sendDeleteRequestIncomingEvent(UUID requesterId, UUID recipientId) {
        List<UUID> userIdList = Collections.singletonList(recipientId);
        WsEventDTO<UUID> eventDTO = new WsEventDTO<>(userIdList, requesterId);
        sendDeleteRequestIncomingEventAsync(eventDTO);
    }

    public void sendDeleteRequestOutcomingEvent(UUID requesterId, UUID recipientId) {
        List<UUID> userIdList = Collections.singletonList(requesterId);
        WsEventDTO<UUID> eventDTO = new WsEventDTO<>(userIdList, recipientId);
        sendDeleteRequestOutcomingEventAsync(eventDTO);
    }

    public void sendDeleteRelationEvent(UUID firstUserId, UUID secondUserId) {
        List<UUID> userIdList = Collections.singletonList(firstUserId);
        WsEventDTO<UUID> eventDTO = new WsEventDTO<>(userIdList, secondUserId);
        sendDeleteRelationEventAsync(eventDTO);
    }


    @Async
    public void sendRequestIncomingEventAsync(WsEventDTO<UUID> event) {
        wsServiceClient.sendRequestIncomingEvent(event);
    }

    @Async
    public void sendRequestOutcomingEventAsync(WsEventDTO<UUID> event) {
        wsServiceClient.sendRequestOutcomingEvent(event);
    }

    @Async
    public void sendAcceptIncomingEventAsync(WsEventDTO<UUID> event) {
        wsServiceClient.sendAcceptIncomingEvent(event);
    }

    @Async
    public void sendAcceptOutcomingEventAsync(WsEventDTO<UUID> event) {
        wsServiceClient.sendAcceptOutcomingEvent(event);
    }

    @Async
    public void sendDeleteRequestIncomingEventAsync(WsEventDTO<UUID> event) {
        wsServiceClient.sendDeleteRequestIncomingEvent(event);
    }

    @Async
    public void sendDeleteRequestOutcomingEventAsync(WsEventDTO<UUID> event) {
        wsServiceClient.sendDeleteRequestOutcomingEvent(event);
    }

    @Async
    public void sendDeleteRelationEventAsync(WsEventDTO<UUID> event) {
        wsServiceClient.sendDeleteRelationEvent(event);
    }

}
