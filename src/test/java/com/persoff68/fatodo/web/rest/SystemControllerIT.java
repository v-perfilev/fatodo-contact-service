package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FatodoContactServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestRelation;
import com.persoff68.fatodo.builder.TestRequest;
import com.persoff68.fatodo.model.Relation;
import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.repository.RelationRepository;
import com.persoff68.fatodo.repository.RequestRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoContactServiceApplication.class)
@AutoConfigureMockMvc
class SystemControllerIT {
    private static final String ENDPOINT = "/api/system";

    private static final String USER_1_ID = "98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d";
    private static final String USER_2_ID = "8d583dfd-acfb-4481-80e6-0b46170e2a18";
    private static final String USER_3_ID = "5b8bfe7e-7651-4d39-a70c-22c997e376b1";

    @Autowired
    MockMvc mvc;

    @Autowired
    RequestRepository requestRepository;
    @Autowired
    RelationRepository relationRepository;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        Request requestOneTwo = TestRequest.defaultBuilder()
                .id(null)
                .requesterId(UUID.fromString(USER_1_ID))
                .recipientId(UUID.fromString(USER_2_ID))
                .build().toParent();

        requestRepository.save(requestOneTwo);

        Relation relationTwoThree = TestRelation.defaultBuilder()
                .id(null)
                .firstUserId(UUID.fromString(USER_2_ID))
                .secondUserId(UUID.fromString(USER_3_ID))
                .build().toParent();
        Relation relationThreeTwo = TestRelation.defaultBuilder()
                .id(null)
                .firstUserId(UUID.fromString(USER_3_ID))
                .secondUserId(UUID.fromString(USER_2_ID))
                .build().toParent();

        relationRepository.save(relationTwoThree);
        relationRepository.save(relationThreeTwo);
    }

    @AfterEach
    void cleanup() {
        relationRepository.deleteAll();
        requestRepository.deleteAll();
    }

    @Test
    @WithCustomSecurityContext(authority = "ROLE_SYSTEM")
    void testDeleteAccountPermanently_ok() throws Exception {
        String url = ENDPOINT + "/" + USER_2_ID;
        mvc.perform(delete(url))
                .andExpect(status().isOk());
        List<Request> requesterRequestList = requestRepository.findAllByRequesterId(UUID.fromString(USER_2_ID));
        List<Request> recipientRequestList = requestRepository.findAllByRecipientId(UUID.fromString(USER_2_ID));
        List<Relation> relationList = relationRepository.findAllByFirstUserId(UUID.fromString(USER_2_ID));
        assertThat(requesterRequestList).isEmpty();
        assertThat(recipientRequestList).isEmpty();
        assertThat(relationList).isEmpty();
    }

    @Test
    @WithCustomSecurityContext(id = USER_2_ID)
    void testGetAllPageable_ok_withParams() throws Exception {
        String url = ENDPOINT + "/" + USER_2_ID;
        mvc.perform(delete(url))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithAnonymousUser
    void testGetAllPageable_unauthorized() throws Exception {
        String url = ENDPOINT + "/" + USER_2_ID;
        mvc.perform(delete(url))
                .andExpect(status().isUnauthorized());
    }

}
