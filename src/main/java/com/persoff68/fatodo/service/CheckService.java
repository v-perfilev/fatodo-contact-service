package com.persoff68.fatodo.service;

import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.model.Relation;
import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.repository.RelationRepository;
import com.persoff68.fatodo.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CheckService {

    private final RelationRepository relationRepository;
    private final RequestRepository requestRepository;
    private final UserServiceClient userServiceClient;

    public boolean doesRelationExist(UUID firstUserId, UUID secondUserId) {
        List<Relation> relationList = relationRepository.findAllByUserIds(firstUserId, secondUserId);
        return !relationList.isEmpty();
    }

    public boolean doesRequestExist(UUID firstUserId, UUID secondUserId) {
        List<Request> requestList = requestRepository.findAllByUserIds(firstUserId, secondUserId);
        return !requestList.isEmpty();
    }

    public boolean doesUserIdExist(UUID userId) {
        return userServiceClient.doesIdExist(userId);
    }

    public boolean areUsersInContactList(UUID userId, List<UUID> userIdList) {
        List<Relation> relationList = relationRepository.findAllByFirstUserId(userId);
        return relationList.stream()
                .map(Relation::getSecondUserId)
                .collect(Collectors.toSet())
                .containsAll(userIdList);
    }
}
