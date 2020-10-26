package com.persoff68.fatodo.web.rest;

import com.persoff68.fatodo.model.dto.ContactRelationDTO;
import com.persoff68.fatodo.model.mapper.ContactRelationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(ContactRelationController.ENDPOINT)
@RequiredArgsConstructor
public class ContactRelationController {
    static final String ENDPOINT = "/api/contact-request";

    private final ContactRelationMapper contactRelationMapper;

    public ResponseEntity<List<ContactRelationDTO>> getRelations() {

        return null;
    }

    public ResponseEntity<Void> removeRelation() {

        return null;
    }

}
