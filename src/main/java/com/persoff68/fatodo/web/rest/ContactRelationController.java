package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.ContactRelation;
import com.persoff68.fatodo.model.dto.ContactRelationDTO;
import com.persoff68.fatodo.model.mapper.ContactRelationMapper;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.ContactRelationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ContactRelationController.ENDPOINT)
@RequiredArgsConstructor
public class ContactRelationController {
    static final String ENDPOINT = "/api/contact-request";

    private final ContactRelationService contactRelationService;
    private final ContactRelationMapper contactRelationMapper;

    @GetMapping
    public ResponseEntity<List<ContactRelationDTO>> getRelations() {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        List<ContactRelation> contactRelationList = contactRelationService.getRelationsByUser(userId);
        List<ContactRelationDTO> contactRelationDTOList = contactRelationList.stream()
                .map(contactRelationMapper::pojoToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(contactRelationDTOList);
    }

    @DeleteMapping(value = "/{secondUserId}")
    public ResponseEntity<Void> removeRelation(@PathVariable UUID secondUserId) {
        UUID firstUserId = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        contactRelationService.deleteByUsers(firstUserId, secondUserId);
        return ResponseEntity.ok().build();
    }

}
