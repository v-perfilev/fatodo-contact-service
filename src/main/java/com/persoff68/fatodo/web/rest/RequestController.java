package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.mapper.RequestMapper;
import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.model.dto.RequestDTO;
import com.persoff68.fatodo.model.vm.RequestVM;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(RequestController.ENDPOINT)
@RequiredArgsConstructor
public class RequestController {
    static final String ENDPOINT = "/api/request";

    private final RequestService requestService;
    private final RequestMapper requestMapper;

    @GetMapping(value = "/outcoming")
    public ResponseEntity<List<RequestDTO>> getOutcomingRequests() {
        UUID id = SecurityUtils.getCurrentId().orElseThrow(UnauthorizedException::new);
        List<Request> requestList = requestService.getAllOutcoming(id);
        List<RequestDTO> requestDTOList = requestList.stream()
                .map(requestMapper::pojoToDTO)
                .toList();
        return ResponseEntity.ok(requestDTOList);
    }

    @GetMapping(value = "/incoming")
    public ResponseEntity<List<RequestDTO>> getIncomingRequests() {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        List<Request> requestList = requestService.getAllIncoming(userId);
        List<RequestDTO> requestDTOList = requestList.stream()
                .map(requestMapper::pojoToDTO)
                .toList();
        return ResponseEntity.ok(requestDTOList);
    }

    @PostMapping
    public ResponseEntity<Void> sendRequest(@RequestBody @Valid RequestVM requestVM) {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        requestService.send(userId, requestVM.getRecipientId(), requestVM.getMessage());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping(value = "/{recipientId}")
    public ResponseEntity<Void> removeRequest(@PathVariable UUID recipientId) {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        requestService.remove(userId, recipientId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{requesterId}/accept")
    public ResponseEntity<Void> acceptRequests(@PathVariable UUID requesterId) {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        requestService.accept(requesterId, userId);
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/{requesterId}/decline")
    public ResponseEntity<Void> declineRequests(@PathVariable UUID requesterId) {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        requestService.remove(requesterId, userId, userId);
        return ResponseEntity.ok().build();
    }

}
