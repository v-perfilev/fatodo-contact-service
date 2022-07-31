package com.persoff68.fatodo.service.client;

import com.persoff68.fatodo.client.WsServiceClient;
import com.persoff68.fatodo.model.dto.ClearEventDTO;
import com.persoff68.fatodo.model.dto.RequestEventDTO;
import com.persoff68.fatodo.model.dto.WsEventDTO;
import com.persoff68.fatodo.model.dto.constant.ClearEventType;
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
        RequestEventDTO requestEventDTO = new RequestEventDTO(requesterId, recipientId);
        WsEventDTO<RequestEventDTO> eventDTO = new WsEventDTO<>(userIdList, requestEventDTO);
        sendRequestIncomingEventAsync(eventDTO);
    }

    public void sendRequestOutcomingEvent(UUID requesterId, UUID recipientId) {
        List<UUID> userIdList = Collections.singletonList(requesterId);
        RequestEventDTO requestEventDTO = new RequestEventDTO(requesterId, recipientId);
        WsEventDTO<RequestEventDTO> eventDTO = new WsEventDTO<>(userIdList, requestEventDTO);
        sendRequestOutcomingEventAsync(eventDTO);
    }

    public void sendAcceptIncomingEvent(UUID requesterId, UUID recipientId) {
        List<UUID> userIdList = Collections.singletonList(recipientId);
        RequestEventDTO requestEventDTO = new RequestEventDTO(requesterId, recipientId);
        WsEventDTO<RequestEventDTO> eventDTO = new WsEventDTO<>(userIdList, requestEventDTO);
        sendAcceptIncomingEventAsync(eventDTO);
    }

    public void sendAcceptOutcomingEvent(UUID requesterId, UUID recipientId) {
        List<UUID> userIdList = Collections.singletonList(requesterId);
        RequestEventDTO requestEventDTO = new RequestEventDTO(requesterId, recipientId);
        WsEventDTO<RequestEventDTO> eventDTO = new WsEventDTO<>(userIdList, requestEventDTO);
        sendAcceptOutcomingEventAsync(eventDTO);
    }

    public void sendClearEvent(ClearEventType type, UUID id, List<UUID> userIdList) {
        ClearEventDTO clearEventDTO = new ClearEventDTO();
        clearEventDTO.setType(type);
        clearEventDTO.setId(id);
        WsEventDTO<ClearEventDTO> eventDTO = new WsEventDTO<>(userIdList, clearEventDTO);
        sendClearEventAsync(eventDTO);
    }

    @Async
    public void sendRequestIncomingEventAsync(WsEventDTO<RequestEventDTO> event) {
        wsServiceClient.sendRequestIncomingEvent(event);
    }

    @Async
    public void sendRequestOutcomingEventAsync(WsEventDTO<RequestEventDTO> event) {
        wsServiceClient.sendRequestOutcomingEvent(event);
    }

    @Async
    public void sendAcceptIncomingEventAsync(WsEventDTO<RequestEventDTO> event) {
        wsServiceClient.sendAcceptIncomingEvent(event);
    }

    @Async
    public void sendAcceptOutcomingEventAsync(WsEventDTO<RequestEventDTO> event) {
        wsServiceClient.sendAcceptOutcomingEvent(event);
    }

    @Async
    public void sendClearEventAsync(WsEventDTO<ClearEventDTO> event) {
        wsServiceClient.sendClearEvent(event);
    }

}
