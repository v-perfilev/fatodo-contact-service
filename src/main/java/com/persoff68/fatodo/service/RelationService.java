package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.Relation;
import com.persoff68.fatodo.repository.RelationRepository;
import com.persoff68.fatodo.service.client.EventService;
import com.persoff68.fatodo.service.client.WsService;
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
    private final EventService eventService;
    private final WsService wsService;

    public List<Relation> getRelationsByUser(UUID id) {
        return relationRepository.findAllByFirstUserId(id);
    }

    public List<Relation> getCommonRelationsByUsers(UUID firstUserId, UUID secondUserId) {
        List<Relation> firstUserRelationList = relationRepository.findAllByFirstUserId(firstUserId);
        List<Relation> secondUserRelationList = relationRepository.findAllByFirstUserId(secondUserId);
        List<UUID> relationUserIdList = secondUserRelationList.stream()
                .map(Relation::getSecondUserId)
                .toList();
        return firstUserRelationList.stream()
                .filter(g -> relationUserIdList.contains(g.getSecondUserId()))
                .toList();
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
        eventService.deleteContactEventsForUserEvents(firstUserId, secondUserId);

        // WS
        wsService.sendDeleteRelationEvent(firstUserId, secondUserId);
        wsService.sendDeleteRelationEvent(secondUserId, firstUserId);
    }


}
