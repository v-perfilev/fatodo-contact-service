package com.persoff68.fatodo.service.client;

import com.persoff68.fatodo.client.WsServiceClient;
import com.persoff68.fatodo.mapper.RelationMapper;
import com.persoff68.fatodo.mapper.RequestMapper;
import com.persoff68.fatodo.model.Relation;
import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.model.constant.WsEventType;
import com.persoff68.fatodo.model.dto.RelationDTO;
import com.persoff68.fatodo.model.dto.RequestDTO;
import com.persoff68.fatodo.model.dto.WsEventDTO;
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

    public void sendRequestIncomingEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        WsEventDTO wsEventDTO = new WsEventDTO(userIdList, WsEventType.CONTACT_REQUEST_INCOMING,
                requestDTO, request.getRequesterId());
        wsServiceClient.sendEvent(wsEventDTO);
    }

    public void sendRequestOutcomingEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRequesterId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        WsEventDTO wsEventDTO = new WsEventDTO(userIdList, WsEventType.CONTACT_REQUEST_OUTCOMING,
                requestDTO, request.getRequesterId());
        wsServiceClient.sendEvent(wsEventDTO);
    }

    public void sendAcceptIncomingEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        WsEventDTO wsEventDTO = new WsEventDTO(userIdList, WsEventType.CONTACT_ACCEPT_INCOMING,
                requestDTO, request.getRecipientId());
        wsServiceClient.sendEvent(wsEventDTO);
    }

    public void sendAcceptOutcomingEvent(Request request) {
        List<UUID> userIdList = List.of(request.getRequesterId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        WsEventDTO wsEventDTO = new WsEventDTO(userIdList, WsEventType.CONTACT_ACCEPT_OUTCOMING,
                requestDTO, request.getRecipientId());
        wsServiceClient.sendEvent(wsEventDTO);
    }

    public void sendDeleteIncomingEvent(Request request, UUID userId) {
        List<UUID> userIdList = List.of(request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        WsEventDTO wsEventWithUsersDTO = new WsEventDTO(userIdList, WsEventType.CONTACT_DELETE_INCOMING,
                requestDTO, userId);
        wsServiceClient.sendEvent(wsEventWithUsersDTO);
    }

    public void sendDeleteOutcomingEvent(Request request, UUID userId) {
        List<UUID> userIdList = List.of(request.getRequesterId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        WsEventDTO wsEventWithUsersDTO = new WsEventDTO(userIdList, WsEventType.CONTACT_DELETE_OUTCOMING,
                requestDTO, userId);
        wsServiceClient.sendEvent(wsEventWithUsersDTO);
    }

    public void sendDeleteRelationEvent(Relation relation, UUID userId) {
        List<UUID> userIdList = List.of(relation.getFirstUserId(), relation.getSecondUserId());
        RelationDTO relationDTO = relationMapper.pojoToDTO(relation);
        WsEventDTO wsEventWithUsersDTO = new WsEventDTO(userIdList, WsEventType.CONTACT_DELETE,
                relationDTO, userId);
        wsServiceClient.sendEvent(wsEventWithUsersDTO);
    }

}
