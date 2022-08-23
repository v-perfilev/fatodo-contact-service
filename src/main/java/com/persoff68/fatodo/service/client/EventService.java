package com.persoff68.fatodo.service.client;

import com.persoff68.fatodo.client.EventServiceClient;
import com.persoff68.fatodo.mapper.RelationMapper;
import com.persoff68.fatodo.mapper.RequestMapper;
import com.persoff68.fatodo.model.Relation;
import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.model.constant.EventType;
import com.persoff68.fatodo.model.dto.EventDTO;
import com.persoff68.fatodo.model.dto.RelationDTO;
import com.persoff68.fatodo.model.dto.RequestDTO;
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

    public void sendRequestIncomingEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        EventDTO eventDTO = new EventDTO(userIdList, EventType.CONTACT_REQUEST_INCOMING,
                requestDTO, request.getRequesterId());
        eventServiceClient.addEvent(eventDTO);
    }

    public void sendRequestOutcomingEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRequesterId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        EventDTO eventDTO = new EventDTO(userIdList, EventType.CONTACT_REQUEST_OUTCOMING,
                requestDTO, request.getRequesterId());
        eventServiceClient.addEvent(eventDTO);
    }

    public void sendAcceptIncomingEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        EventDTO eventDTO = new EventDTO(userIdList, EventType.CONTACT_ACCEPT_INCOMING,
                requestDTO, request.getRecipientId());
        eventServiceClient.addEvent(eventDTO);
    }

    public void sendAcceptOutcomingEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRequesterId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        EventDTO eventDTO = new EventDTO(userIdList, EventType.CONTACT_ACCEPT_OUTCOMING,
                requestDTO, request.getRecipientId());
        eventServiceClient.addEvent(eventDTO);
    }

    public void sendDeleteIncomingEvent(Request request, UUID userId) {
        List<UUID> userIdList = List.of(request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        EventDTO wsEventWithUsersDTO = new EventDTO(userIdList, EventType.CONTACT_DELETE_INCOMING,
                requestDTO, userId);
        eventServiceClient.addEvent(wsEventWithUsersDTO);
    }

    public void sendDeleteOutcomingEvent(Request request, UUID userId) {
        List<UUID> userIdList = List.of(request.getRequesterId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        EventDTO wsEventWithUsersDTO = new EventDTO(userIdList, EventType.CONTACT_DELETE_OUTCOMING,
                requestDTO, userId);
        eventServiceClient.addEvent(wsEventWithUsersDTO);
    }

    public void sendDeleteRelationEvent(Relation relation, UUID userId) {
        List<UUID> userIdList = List.of(relation.getFirstUserId(), relation.getSecondUserId());
        RelationDTO relationDTO = relationMapper.pojoToDTO(relation);
        EventDTO wsEventWithUsersDTO = new EventDTO(userIdList, EventType.CONTACT_DELETE,
                relationDTO, userId);
        eventServiceClient.addEvent(wsEventWithUsersDTO);
    }

}
