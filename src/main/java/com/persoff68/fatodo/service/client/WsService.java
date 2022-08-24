package com.persoff68.fatodo.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.client.WsServiceClient;
import com.persoff68.fatodo.mapper.RelationMapper;
import com.persoff68.fatodo.mapper.RequestMapper;
import com.persoff68.fatodo.model.Relation;
import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.model.constant.WsEventType;
import com.persoff68.fatodo.model.dto.RelationDTO;
import com.persoff68.fatodo.model.dto.RequestDTO;
import com.persoff68.fatodo.model.dto.event.WsEventDTO;
import com.persoff68.fatodo.service.exception.ModelInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class WsService {

    private final WsServiceClient wsServiceClient;
    private final RequestMapper requestMapper;
    private final RelationMapper relationMapper;
    private final ObjectMapper objectMapper;

    public void sendRequestIncomingEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        String payload = serialize(requestDTO);
        WsEventDTO wsEventDTO = new WsEventDTO(userIdList, WsEventType.CONTACT_REQUEST_INCOMING, payload,
                request.getRequesterId());
        wsServiceClient.sendEvent(wsEventDTO);
    }

    public void sendRequestOutcomingEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRequesterId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        String payload = serialize(requestDTO);
        WsEventDTO wsEventDTO = new WsEventDTO(userIdList, WsEventType.CONTACT_REQUEST_OUTCOMING, payload,
                request.getRequesterId());
        wsServiceClient.sendEvent(wsEventDTO);
    }

    public void sendAcceptIncomingEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        String payload = serialize(requestDTO);
        WsEventDTO wsEventDTO = new WsEventDTO(userIdList, WsEventType.CONTACT_ACCEPT_INCOMING, payload,
                request.getRecipientId());
        wsServiceClient.sendEvent(wsEventDTO);
    }

    public void sendAcceptOutcomingEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRequesterId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        String payload = serialize(requestDTO);
        WsEventDTO wsEventDTO = new WsEventDTO(userIdList, WsEventType.CONTACT_ACCEPT_OUTCOMING, payload,
                request.getRecipientId());
        wsServiceClient.sendEvent(wsEventDTO);
    }

    public void sendDeleteIncomingEvent(Request request, UUID userId) {
        List<UUID> userIdList = List.of(request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        String payload = serialize(requestDTO);
        WsEventDTO wsEventWithUsersDTO = new WsEventDTO(userIdList, WsEventType.CONTACT_DELETE_INCOMING, payload,
                userId);
        wsServiceClient.sendEvent(wsEventWithUsersDTO);
    }

    public void sendDeleteOutcomingEvent(Request request, UUID userId) {
        List<UUID> userIdList = List.of(request.getRequesterId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        String payload = serialize(requestDTO);
        WsEventDTO wsEventWithUsersDTO = new WsEventDTO(userIdList, WsEventType.CONTACT_DELETE_OUTCOMING, payload,
                userId);
        wsServiceClient.sendEvent(wsEventWithUsersDTO);
    }

    public void sendDeleteRelationEvent(Relation relation, UUID userId) {
        List<UUID> userIdList = List.of(relation.getFirstUserId(), relation.getSecondUserId());
        RelationDTO relationDTO = relationMapper.pojoToDTO(relation);
        String payload = serialize(relationDTO);
        WsEventDTO wsEventWithUsersDTO = new WsEventDTO(userIdList, WsEventType.CONTACT_DELETE, payload, userId);
        wsServiceClient.sendEvent(wsEventWithUsersDTO);
    }

    private String serialize(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new ModelInvalidException();
        }
    }

}
