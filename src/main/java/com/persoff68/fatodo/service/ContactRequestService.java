package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.ContactRequest;
import com.persoff68.fatodo.repository.ContactRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactRequestService {

    private final ContactRequestRepository contactRequestRepository;

    public List<ContactRequest> getAllOutcoming(UUID userId) {
        return null;
    }

    public List<ContactRequest> getAllIncoming(UUID userId) {
        return null;
    }

    public void send(UUID requesterId, UUID recipientId) {
    }

    @Transactional
    public void accept(UUID requesterId, UUID recipientId) {
    }

    public void remove(UUID requesterId, UUID recipientId) {
    }

}
