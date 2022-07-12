package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.model.dto.RequestDTO;
import com.persoff68.fatodo.mapper.RequestMapper;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(RequestResource.ENDPOINT)
@RequiredArgsConstructor
public class RequestResource {
    static final String ENDPOINT = "/api/requests";

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

}
