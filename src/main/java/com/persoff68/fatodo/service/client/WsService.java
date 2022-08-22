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

import java.util.Collections;
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
        List<UUID> userIdList = Collections.singletonList(request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        WsEventDTO wsEventWithUsersDTO = new WsEventDTO(userIdList,
                WsEventType.CONTACT_REQUEST_INCOMING, requestDTO);
        wsServiceClient.sendEvent(wsEventWithUsersDTO);
    }

    public void sendRequestOutcomingEvent(Request request) {
        List<UUID> userIdList = Collections.singletonList(request.getRequesterId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        WsEventDTO wsEventWithUsersDTO = new WsEventDTO(userIdList,
                WsEventType.CONTACT_REQUEST_OUTCOMING, requestDTO);
        wsServiceClient.sendEvent(wsEventWithUsersDTO);
    }

    public void sendAcceptIncomingEvent(Request request) {
        List<UUID> userIdList = Collections.singletonList(request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        WsEventDTO wsEventWithUsersDTO = new WsEventDTO(userIdList,
                WsEventType.CONTACT_ACCEPT_INCOMING, requestDTO);
        wsServiceClient.sendEvent(wsEventWithUsersDTO);
    }

    public void sendAcceptOutcomingEvent(Request request) {
        List<UUID> userIdList = Collections.singletonList(request.getRequesterId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        WsEventDTO wsEventWithUsersDTO = new WsEventDTO(userIdList,
                WsEventType.CONTACT_ACCEPT_OUTCOMING, requestDTO);
        wsServiceClient.sendEvent(wsEventWithUsersDTO);
    }

    public void sendDeleteIncomingEvent(Request request) {
        List<UUID> userIdList = Collections.singletonList(request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        WsEventDTO wsEventWithUsersDTO = new WsEventDTO(userIdList,
                WsEventType.CONTACT_DELETE_INCOMING, requestDTO);
        wsServiceClient.sendEvent(wsEventWithUsersDTO);
    }

    public void sendDeleteRequestOutcomingEvent(Request request) {
        List<UUID> userIdList = Collections.singletonList(request.getRecipientId());
        RequestDTO requestDTO = requestMapper.pojoToDTO(request);
        WsEventDTO wsEventWithUsersDTO = new WsEventDTO(userIdList,
                WsEventType.CONTACT_DELETE_OUTCOMING, requestDTO);
        wsServiceClient.sendEvent(wsEventWithUsersDTO);
    }

    public void sendDeleteRelationEvent(Relation relation) {
        List<UUID> userIdList = Collections.singletonList(relation.getFirstUserId());
        RelationDTO relationDTO = relationMapper.pojoToDTO(relation);
        WsEventDTO wsEventWithUsersDTO = new WsEventDTO(userIdList,
                WsEventType.CONTACT_DELETE, relationDTO);
        wsServiceClient.sendEvent(wsEventWithUsersDTO);
    }

}
