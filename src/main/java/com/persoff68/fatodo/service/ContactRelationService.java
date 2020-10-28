package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.ContactRelation;
import com.persoff68.fatodo.repository.ContactRelationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ContactRelationService {

    private final ContactRelationRepository contactRelationRepository;

    public List<ContactRelation> getRelationsByUser(UUID id) {
        return contactRelationRepository.findAllByFirstUserId(id);
    }

    @Transactional
    public void addForUsers(UUID firstUserId, UUID secondUserId) {
        ContactRelation firstContactRelation = new ContactRelation();
        firstContactRelation.setFirstUserId(firstUserId);
        firstContactRelation.setSecondUserId(secondUserId);
        ContactRelation secondContactRelation = new ContactRelation();
        secondContactRelation.setFirstUserId(firstUserId);
        secondContactRelation.setSecondUserId(secondUserId);
        List<ContactRelation> contactRelationList = List.of(firstContactRelation, secondContactRelation);
        contactRelationRepository.saveAll(contactRelationList);
    }

    @Transactional
    public void deleteByUsers(UUID firstUserId, UUID secondUserId) {
        contactRelationRepository.deleteByFirstUserIdAndSecondUserId(firstUserId, secondUserId);
        contactRelationRepository.deleteByFirstUserIdAndSecondUserId(secondUserId, firstUserId);
    }

}
