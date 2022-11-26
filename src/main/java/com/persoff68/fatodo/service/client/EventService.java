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

    public void sendRequestEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRequesterId(), request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        String payload = serialize(requestDTO);
        EventDTO eventDTO = new EventDTO(userIdList, EventType.CONTACT_REQUEST, payload,
                request.getRequesterId());
        eventServiceClient.addEvent(eventDTO);
    }

    public void sendAcceptEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRequesterId(), request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        String payload = serialize(requestDTO);
        EventDTO eventDTO = new EventDTO(userIdList, EventType.CONTACT_ACCEPT, payload,
                request.getRecipientId());
        eventServiceClient.addEvent(eventDTO);
    }

    public void sendDeclineEvent(Request request, UUID userId) {
        List<UUID> userIdList = List.of(request.getRequesterId(), request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        String payload = serialize(requestDTO);
        EventDTO wsEventWithUsersDTO = new EventDTO(userIdList, EventType.CONTACT_DECLINE, payload, userId);
        eventServiceClient.addEvent(wsEventWithUsersDTO);
    }

    public void sendDeleteRelationEvent(Relation relation, UUID userId) {
        List<UUID> userIdList = List.of(relation.getFirstUserId(), relation.getSecondUserId());
        RelationDTO relationDTO = relationMapper.pojoToDTO(relation);
        String payload = serialize(relationDTO);
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
