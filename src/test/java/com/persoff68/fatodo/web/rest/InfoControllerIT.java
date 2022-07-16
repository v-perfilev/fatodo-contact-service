package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FatodoContactServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestRelation;
import com.persoff68.fatodo.builder.TestRequest;
import com.persoff68.fatodo.model.Relation;
import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.model.dto.ContactInfoDTO;
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
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoContactServiceApplication.class)
@AutoConfigureMockMvc
class InfoControllerIT {
    private static final String ENDPOINT = "/api/info";

    private static final UUID USER_1_ID = UUID.fromString("98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d");
    private static final UUID USER_2_ID = UUID.randomUUID();
    private static final UUID USER_3_ID = UUID.randomUUID();
    private static final UUID USER_4_ID = UUID.randomUUID();

    @Autowired
    MockMvc mvc;

    @Autowired
    RelationRepository relationRepository;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        Relation relationOneTwo = TestRelation.defaultBuilder()
                .id(null)
                .firstUserId(USER_1_ID)
                .secondUserId(USER_2_ID)
                .build().toParent();
        Relation relationTwoOne = TestRelation.defaultBuilder()
                .id(null)
                .firstUserId(USER_2_ID)
                .secondUserId(USER_1_ID)
                .build().toParent();

        relationRepository.save(relationOneTwo);
        relationRepository.save(relationTwoOne);

        Request requestOneThree = TestRequest.defaultBuilder()
                .id(null)
                .requesterId(USER_1_ID)
                .recipientId(USER_3_ID)
                .build().toParent();

        Request requestFourOne = TestRequest.defaultBuilder()
                .id(null)
                .requesterId(USER_4_ID)
                .recipientId(USER_1_ID)
                .build().toParent();

        requestRepository.save(requestOneThree);
        requestRepository.save(requestFourOne);
    }

    @AfterEach
    void cleanup() {
        relationRepository.deleteAll();
        requestRepository.deleteAll();
    }

    @Test
    @WithCustomSecurityContext(id = "98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d")
    void testGetInfo_ok() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        ContactInfoDTO dto = objectMapper.readValue(resultString, ContactInfoDTO.class);
        assertThat(dto.getRelationCount()).isEqualTo(1);
        assertThat(dto.getOutcomingRequestCount()).isEqualTo(1);
        assertThat(dto.getIncomingRequestCount()).isEqualTo(1);
    }

    @Test
    @WithAnonymousUser
    void testGetInfo_unauthorized() throws Exception {
        mvc.perform(get(ENDPOINT))
                .andExpect(status().isUnauthorized());
    }

}
