package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.ContactRequest;
import com.persoff68.fatodo.model.dto.ContactRequestDTO;
import com.persoff68.fatodo.model.mapper.ContactRequestMapper;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.ContactRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(ContactRequestController.ENDPOINT)
@RequiredArgsConstructor
public class ContactRequestController {
    static final String ENDPOINT = "/api/contact-request";

    private final ContactRequestService contactRequestService;
    private final ContactRequestMapper contactRequestMapper;

    @GetMapping(value = "/outcoming")
    public ResponseEntity<List<ContactRequestDTO>> getOutcomingRequests() {
        UUID id = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        List<ContactRequest> contactRequestList = contactRequestService.getAllOutcoming(id);
        List<ContactRequestDTO> contactRequestDTOList = contactRequestList.stream()
                .map(contactRequestMapper::pojoToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(contactRequestDTOList);
    }

    @GetMapping(value = "/incoming")
    public ResponseEntity<List<ContactRequestDTO>> getIncomingRequests() {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        List<ContactRequest> contactRequestList = contactRequestService.getAllIncoming(userId);
        List<ContactRequestDTO> contactRequestDTOList = contactRequestList.stream()
                .map(contactRequestMapper::pojoToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(contactRequestDTOList);
    }

    @GetMapping(value = "/send/{recipientId}")
    public ResponseEntity<Void> sendRequest(@PathVariable UUID recipientId) {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        contactRequestService.send(userId, recipientId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/remove/{recipientId}")
    public ResponseEntity<Void> removeRequest(@PathVariable UUID recipientId) {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        contactRequestService.remove(userId, recipientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/accept/{requesterId}")
    public ResponseEntity<Void> acceptRequests(@PathVariable UUID requesterId) {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        contactRequestService.accept(requesterId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/decline/{requesterId}")
    public ResponseEntity<Void> declineRequests(@PathVariable UUID requesterId) {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        contactRequestService.remove(requesterId, userId);
        return ResponseEntity.ok().build();
    }

}
