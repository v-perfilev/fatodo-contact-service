package com.persoff68.fatodo.service.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.client.EventServiceClient;
import com.persoff68.fatodo.mapper.RelationMapper;
import com.persoff68.fatodo.mapper.RequestMapper;
import com.persoff68.fatodo.model.Relation;
import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.RelationDTO;
import com.persoff68.fatodo.model.dto.RequestDTO;
import com.persoff68.fatodo.model.dto.event.EventDTO;
import com.persoff68.fatodo.service.exception.ModelInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Async
public class EventService {

    private final EventServiceClient eventServiceClient;
    private final RequestMapper requestMapper;
    private final RelationMapper relationMapper;
    private final ObjectMapper objectMapper;

    public void sendRequestIncomingEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        String payload = serialize(requestDTO);
        EventDTO eventDTO = new EventDTO(userIdList, EventType.CONTACT_REQUEST_INCOMING, payload,
                request.getRequesterId());
        eventServiceClient.addEvent(eventDTO);
    }

    public void sendRequestOutcomingEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRequesterId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        String payload = serialize(requestDTO);
        EventDTO eventDTO = new EventDTO(userIdList, EventType.CONTACT_REQUEST_OUTCOMING, payload,
                request.getRequesterId());
        eventServiceClient.addEvent(eventDTO);
    }

    public void sendAcceptIncomingEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        String payload = serialize(requestDTO);
        EventDTO eventDTO = new EventDTO(userIdList, EventType.CONTACT_ACCEPT_INCOMING, payload,
                request.getRecipientId());
        eventServiceClient.addEvent(eventDTO);
    }

    public void sendAcceptOutcomingEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRequesterId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        String payload = serialize(requestDTO);
        EventDTO eventDTO = new EventDTO(userIdList, EventType.CONTACT_ACCEPT_OUTCOMING, payload,
                request.getRecipientId());
        eventServiceClient.addEvent(eventDTO);
    }

    public void sendDeleteIncomingEvent(Request request, UUID userId) {
        List<UUID> userIdList = List.of(request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        String payload = serialize(requestDTO);
        EventDTO wsEventWithUsersDTO = new EventDTO(userIdList, EventType.CONTACT_DELETE_INCOMING, payload, userId);
        eventServiceClient.addEvent(wsEventWithUsersDTO);
    }

    public void sendDeleteOutcomingEvent(Request request, UUID userId) {
        List<UUID> userIdList = List.of(request.getRequesterId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        String payload = serialize(requestDTO);
        EventDTO wsEventWithUsersDTO = new EventDTO(userIdList, EventType.CONTACT_DELETE_OUTCOMING, payload, userId);
        eventServiceClient.addEvent(wsEventWithUsersDTO);
    }

    public void sendDeleteRelationEvent(Relation relation, UUID userId) {
        List<UUID> userIdList = List.of(relation.getFirstUserId(), relation.getSecondUserId());
        RelationDTO relationDTO = relationMapper.pojoToDTO(relation);
        String payload = serialize(relation);
        EventDTO wsEventWithUsersDTO = new EventDTO(userIdList, EventType.CONTACT_DELETE, payload, userId);
        eventServiceClient.addEvent(wsEventWithUsersDTO);
    }

    private String serialize(Object value) {
        try {
            return objectMapper.writeValueAsString(value);
        } catch (JsonProcessingException e) {
            throw new ModelInvalidException();
        }
    }

}
