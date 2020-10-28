package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.ContactRequest;
import com.persoff68.fatodo.repository.ContactRequestRepository;
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
public class ContactRequestService {

    private final ContactRequestRepository contactRequestRepository;
    private final ContactRelationService contactRelationService;
    private final CheckService checkService;

    public List<ContactRequest> getAllOutcoming(UUID userId) {
        return contactRequestRepository.findAllByRequesterId(userId);
    }

    public List<ContactRequest> getAllIncoming(UUID userId) {
        return contactRequestRepository.findAllByRecipientId(userId);
    }

    public void send(UUID requesterId, UUID recipientId) {
        boolean doesRelationExist = checkService.doesRelationExist(requesterId, recipientId);
        if (doesRelationExist) {
            throw new RelationAlreadyExistsException();
        }
        boolean doesRequestExist = checkService.doesRequestExist(requesterId, recipientId);
        if (doesRequestExist) {
            throw new RequestAlreadyExistsException();
        }
    }

    @Transactional
    public void accept(UUID requesterId, UUID recipientId) {
        this.remove(requesterId, recipientId);
        contactRelationService.addForUsers(requesterId, recipientId);
    }

    public void remove(UUID requesterId, UUID recipientId) {
        ContactRequest contactRequest = contactRequestRepository
                .findByRequesterIdAndRecipientId(requesterId, recipientId)
                .orElseThrow(ModelNotFoundException::new);
        contactRequestRepository.delete(contactRequest);
    }

}
