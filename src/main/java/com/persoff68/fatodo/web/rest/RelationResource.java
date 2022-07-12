package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.Relation;
import com.persoff68.fatodo.model.dto.RelationDTO;
import com.persoff68.fatodo.mapper.RelationMapper;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.RelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(RelationResource.ENDPOINT)
@RequiredArgsConstructor
public class RelationResource {
    static final String ENDPOINT = "/api/relations";

    private final RelationService relationService;
    private final RelationMapper relationMapper;

    @GetMapping
    public ResponseEntity<List<RelationDTO>> getRelations() {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        List<Relation> relationList = relationService.getRelationsByUser(userId);
        List<RelationDTO> relationDTOList = relationList.stream()
                .map(relationMapper::pojoToDTO)
                .toList();
        return ResponseEntity.ok(relationDTOList);
    }

    @GetMapping(value = "/{secondUserId}/user")
    public ResponseEntity<List<RelationDTO>> getCommonRelationsWithUser(@PathVariable UUID secondUserId) {
        UUID firstUserId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        List<Relation> relationList = relationService.getCommonRelationsByUsers(firstUserId, secondUserId);
        List<RelationDTO> relationDTOList = relationList.stream()
                .map(relationMapper::pojoToDTO)
                .toList();
        return ResponseEntity.ok(relationDTOList);
    }

    @DeleteMapping(value = "/{secondUserId}")
    public ResponseEntity<Void> removeRelation(@PathVariable UUID secondUserId) {
        UUID firstUserId = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        relationService.deleteByUsers(firstUserId, secondUserId);
        return ResponseEntity.ok().build();
    }

}
