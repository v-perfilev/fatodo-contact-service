package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.Relation;
import com.persoff68.fatodo.repository.RelationRepository;
import com.persoff68.fatodo.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SystemService {

    private final RequestRepository requestRepository;
    private final RelationRepository relationRepository;

    @Transactional
    public void deleteAccountPermanently(UUID userId) {
        List<Relation> relationList = relationRepository.findAllByFirstUserId(userId);
        requestRepository.deleteAllByRequesterId(userId);
        requestRepository.deleteAllByRecipientId(userId);
        relationRepository.deleteAllByUserId(userId, relationList);
    }

}
