package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.ChatServiceClient;
import com.persoff68.fatodo.model.Message;
import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.repository.RequestRepository;
import com.persoff68.fatodo.service.client.EventService;
import com.persoff68.fatodo.service.client.WsService;
import com.persoff68.fatodo.service.exception.ModelInvalidException;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import com.persoff68.fatodo.service.exception.RelationAlreadyExistsException;
import com.persoff68.fatodo.service.exception.RequestAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final RelationService relationService;
    private final CheckService checkService;
    private final EventService eventService;
    private final WsService wsService;
    private final ChatServiceClient chatServiceClient;

    public List<Request> getAllOutcoming(UUID userId) {
        return requestRepository.findAllByRequesterId(userId);
    }

    public List<Request> getAllIncoming(UUID userId) {
        return requestRepository.findAllByRecipientId(userId);
    }

    public void send(UUID requesterId, UUID recipientId, String message) {
        if (requesterId.equals(recipientId)) {
            throw new ModelInvalidException();
        }
        boolean doesUserExist = checkService.doesUserIdExist(recipientId);
        if (!doesUserExist) {
            throw new ModelNotFoundException();
        }
        boolean doesRelationExist = checkService.doesRelationExist(requesterId, recipientId);
        if (doesRelationExist) {
            throw new RelationAlreadyExistsException();
        }
        boolean doesRequestExist = checkService.doesRequestExist(requesterId, recipientId);
        if (doesRequestExist) {
            throw new RequestAlreadyExistsException();
        }
        Request request = new Request(requesterId, recipientId, message);
        request = requestRepository.save(request);
        if (message != null && !message.isBlank()) {
            Message messageToSend = new Message();
            messageToSend.setText(message);
            chatServiceClient.sendDirect(recipientId, messageToSend);
        }

        // EVENT
        eventService.sendContactSendEvent(requesterId, recipientId);

        // WS
        wsService.sendRequestIncomingEvent(request);
        wsService.sendRequestOutcomingEvent(request);
    }

    @Transactional
    public void accept(UUID requesterId, UUID recipientId) {
        Request request = requestRepository
                .findByRequesterIdAndRecipientId(requesterId, recipientId)
                .orElseThrow(ModelNotFoundException::new);
        requestRepository.delete(request);
        relationService.addForUsers(requesterId, recipientId);
        eventService.sendContactAcceptEvent(requesterId, recipientId);

        // WS
        wsService.sendAcceptIncomingEvent(request);
        wsService.sendAcceptOutcomingEvent(request);
    }

    @Transactional
    public void remove(UUID requesterId, UUID recipientId) {
        Request request = requestRepository
                .findByRequesterIdAndRecipientId(requesterId, recipientId)
                .orElseThrow(ModelNotFoundException::new);
        requestRepository.delete(request);
        eventService.deleteContactEventsForUserEvents(requesterId, recipientId);

        // WS
        wsService.sendDeleteIncomingEvent(request);
        wsService.sendDeleteRequestOutcomingEvent(request);
    }

}
