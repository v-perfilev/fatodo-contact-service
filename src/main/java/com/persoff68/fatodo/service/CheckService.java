package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.ContactRelation;
import com.persoff68.fatodo.model.ContactRequest;
import com.persoff68.fatodo.repository.ContactRelationRepository;
import com.persoff68.fatodo.repository.ContactRequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckService {

    private final ContactRelationRepository contactRelationRepository;
    private final ContactRequestRepository contactRequestRepository;

    public boolean doesRelationExist(UUID firstUserId, UUID secondUserId) {
        List<ContactRelation> contactRelationList =
                contactRelationRepository.findAllByUserIds(firstUserId, secondUserId);
        return !contactRelationList.isEmpty();
    }

    public boolean doesRequestExist(UUID firstUserId, UUID secondUserId) {
        List<ContactRequest> contactRequestList =
                contactRequestRepository.findAllByUserIds(firstUserId, secondUserId);
        return !contactRequestList.isEmpty();
    }

}
