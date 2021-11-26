package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.dto.ContactInfoDTO;
import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.InfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(InfoController.ENDPOINT)
@RequiredArgsConstructor
public class InfoController {
    static final String ENDPOINT = "/api/info";

    private final InfoService infoService;

    @GetMapping
    public ResponseEntity<ContactInfoDTO> getInfo() {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        int relationCount = infoService.getRelationCount(userId);
        int outcomingRequestCount = infoService.getOutcomingRequestCount(userId);
        int incomingRequestCount = infoService.getIncomingRequestCount(userId);
        ContactInfoDTO dto = new ContactInfoDTO(relationCount, outcomingRequestCount, incomingRequestCount);
        return ResponseEntity.ok(dto);
    }

}
