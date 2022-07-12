package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.RequestService;
import com.persoff68.fatodo.model.vm.RequestVM;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(RequestController.ENDPOINT)
@RequiredArgsConstructor
public class RequestController {
    static final String ENDPOINT = "/api/requests";

    private final RequestService requestService;

    @PostMapping(value = "/send")
    public ResponseEntity<Void> sendRequest(@RequestBody @Valid RequestVM requestVM) {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        requestService.send(userId, requestVM.getRecipientId(), requestVM.getMessage());
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
