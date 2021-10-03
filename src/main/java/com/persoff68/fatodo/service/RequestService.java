package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.repository.RequestRepository;
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
        requestRepository.save(request);
    }

    @Transactional
    public void accept(UUID requesterId, UUID recipientId) {
        this.remove(requesterId, recipientId);
        relationService.addForUsers(requesterId, recipientId);
    }

    public void remove(UUID requesterId, UUID recipientId) {
        Request request = requestRepository
                .findByRequesterIdAndRecipientId(requesterId, recipientId)
                .orElseThrow(ModelNotFoundException::new);
        requestRepository.delete(request);
    }

}
