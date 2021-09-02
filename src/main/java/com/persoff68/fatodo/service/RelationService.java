package com.persoff68.fatodo.service;

import com.persoff68.fatodo.config.aop.cache.annotation.CacheableMethod;
import com.persoff68.fatodo.model.Relation;
import com.persoff68.fatodo.repository.RelationRepository;
import com.persoff68.fatodo.service.exception.ModelNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RelationService {

    private final RelationRepository relationRepository;

    @CacheableMethod(cacheName = "relations-by-id", key = "#id")
    public List<Relation> getRelationsByUser(UUID id) {
        return relationRepository.findAllByFirstUserId(id);
    }

    @Transactional
    public void addForUsers(UUID firstUserId, UUID secondUserId) {
        Relation firstRelation = new Relation(firstUserId, secondUserId);
        Relation secondRelation = new Relation(secondUserId, firstUserId);
        List<Relation> relationList = List.of(firstRelation, secondRelation);
        relationRepository.saveAll(relationList);
    }

    @Transactional
    public void deleteByUsers(UUID firstUserId, UUID secondUserId) {
        List<Relation> relationList = relationRepository.findAllByUserIds(firstUserId, secondUserId);
        if (relationList.isEmpty()) {
            throw new ModelNotFoundException();
        }
        relationRepository.deleteAll(relationList);
    }


}