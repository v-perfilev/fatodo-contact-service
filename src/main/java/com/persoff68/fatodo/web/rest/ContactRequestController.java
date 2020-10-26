package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.dto.ContactRequestDTO;
import com.persoff68.fatodo.model.mapper.ContactRequestMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ContactRequestController.ENDPOINT)
@RequiredArgsConstructor
public class ContactRequestController {
    static final String ENDPOINT = "/api/contact-request";

    private final ContactRequestMapper contactRequestMapper;

    public ResponseEntity<List<ContactRequestDTO>> getOutcomingRequests() {

        return null;
    }

    public ResponseEntity<List<ContactRequestDTO>> getIncomingRequests() {

        return null;
    }

    public ResponseEntity<Void> sendRequest() {

        return null;
    }

    public ResponseEntity<Void> removeRequest() {

        return null;
    }

    public ResponseEntity<Void> acceptRequests() {

        return null;
    }

    public ResponseEntity<Void> declineRequests() {

        return null;
    }

}
