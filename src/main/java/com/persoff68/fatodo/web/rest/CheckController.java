package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.security.exception.UnauthorizedException;
import com.persoff68.fatodo.security.util.SecurityUtils;
import com.persoff68.fatodo.service.CheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(CheckController.ENDPOINT)
@RequiredArgsConstructor
public class CheckController {
    static final String ENDPOINT = "/api/check";

    private final CheckService checkService;

    @GetMapping("/contact")
    public ResponseEntity<Boolean> areUsersInContactList(@RequestParam("ids") List<UUID> userIdList) {
        UUID userId = SecurityUtils.getCurrentId()
                .orElseThrow(UnauthorizedException::new);
        boolean areUsersInContactList = checkService.areUsersInContactList(userId, userIdList);
        return ResponseEntity.ok(areUsersInContactList);
    }

}
