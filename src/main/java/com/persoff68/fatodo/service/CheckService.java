package com.persoff68.fatodo.service;

import com.persoff68.fatodo.model.Relation;
import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.repository.RelationRepository;
import com.persoff68.fatodo.repository.RequestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckService {

    private final RelationRepository relationRepository;
    private final RequestRepository requestRepository;

    public boolean doesRelationExist(UUID firstUserId, UUID secondUserId) {
        List<Relation> relationList =
                relationRepository.findAllByUserIds(firstUserId, secondUserId);
        return !relationList.isEmpty();
    }

    public boolean doesRequestExist(UUID firstUserId, UUID secondUserId) {
        List<Request> requestList =
                requestRepository.findAllByUserIds(firstUserId, secondUserId);
        return !requestList.isEmpty();
    }

}
