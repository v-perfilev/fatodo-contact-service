package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.model.dto.RequestDTO;
import com.persoff68.fatodo.model.mapper.RequestMapper;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.RequestService;
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
@RequestMapping(RequestController.ENDPOINT)
@RequiredArgsConstructor
public class RequestController {
    static final String ENDPOINT = "/api/requests";

    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @GetMapping(value = "/outcoming")
    public ResponseEntity<List<RequestDTO>> getOutcomingRequests() {
        UUID id = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        List<Request> requestList = requestService.getAllOutcoming(id);
        List<RequestDTO> requestDTOList = requestList.stream()
                .map(requestMapper::pojoToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(requestDTOList);
    }

    @GetMapping(value = "/incoming")
    public ResponseEntity<List<RequestDTO>> getIncomingRequests() {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        List<Request> requestList = requestService.getAllIncoming(userId);
        List<RequestDTO> requestDTOList = requestList.stream()
                .map(requestMapper::pojoToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(requestDTOList);
    }

    @GetMapping(value = "/send/{recipientId}")
    public ResponseEntity<Void> sendRequest(@PathVariable UUID recipientId) {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        requestService.send(userId, recipientId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping(value = "/remove/{recipientId}")
    public ResponseEntity<Void> removeRequest(@PathVariable UUID recipientId) {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        requestService.remove(userId, recipientId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/accept/{requesterId}")
    public ResponseEntity<Void> acceptRequests(@PathVariable UUID requesterId) {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        requestService.accept(requesterId, userId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/decline/{requesterId}")
    public ResponseEntity<Void> declineRequests(@PathVariable UUID requesterId) {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        requestService.remove(requesterId, userId);
        return ResponseEntity.ok().build();
    }

}
