package com.persoff68.fatodo.service;

import com.persoff68.fatodo.repository.RelationRepository;
import com.persoff68.fatodo.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class InfoService {

    private final RelationRepository relationRepository;
    private final RequestRepository requestRepository;

    public int getRelationCount(UUID userId) {
        return relationRepository.countByFirstUserId(userId);
    }

    public int getOutcomingRequestCount(UUID userId) {
        return requestRepository.countByRequesterId(userId);
    }

    public int getIncomingRequestCount(UUID userId) {
        return requestRepository.countByRecipientId(userId);
    }

}
