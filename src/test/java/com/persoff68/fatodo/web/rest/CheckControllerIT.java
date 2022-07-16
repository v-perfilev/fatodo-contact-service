package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.persoff68.fatodo.FatodoContactServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestRelation;
import com.persoff68.fatodo.model.Relation;
import com.persoff68.fatodo.repository.RelationRepository;
import com.persoff68.fatodo.repository.RequestRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoContactServiceApplication.class)
@AutoConfigureMockMvc
class CheckControllerIT {
    private static final String ENDPOINT = "/api/check";

    private static final UUID USER_1_ID = UUID.fromString("98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d");
    private static final UUID USER_2_ID = UUID.randomUUID();

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
    }

    @AfterEach
    void cleanup() {
        relationRepository.deleteAll();
        requestRepository.deleteAll();
    }

    @Test
    @WithCustomSecurityContext(id = "98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d")
    void testAreUsersInContactList_true() throws Exception {
        String url = ENDPOINT + "/contacts";
        List<UUID> userIdList = List.of(USER_2_ID);
        String requestBody = objectMapper.writeValueAsString(userIdList);
        ResultActions resultActions = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean result = objectMapper.readValue(resultString, Boolean.class);
        assertThat(result).isTrue();
    }

    @Test
    @WithCustomSecurityContext(id = "98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d")
    void testAreUsersInContactList_false_1() throws Exception {
        String url = ENDPOINT + "/contacts";
        List<UUID> userIdList = List.of(USER_2_ID, UUID.randomUUID());
        String requestBody = objectMapper.writeValueAsString(userIdList);
        ResultActions resultActions = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean result = objectMapper.readValue(resultString, Boolean.class);
        assertThat(result).isFalse();
    }

    @Test
    @WithCustomSecurityContext(id = "98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d")
    void testAreUsersInContactList_false_2() throws Exception {
        String url = ENDPOINT + "/contacts";
        List<UUID> userIdList = List.of(UUID.randomUUID());
        String requestBody = objectMapper.writeValueAsString(userIdList);
        ResultActions resultActions = mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        boolean result = objectMapper.readValue(resultString, Boolean.class);
        assertThat(result).isFalse();
    }

    @Test
    @WithAnonymousUser
    void testGetInfo_unauthorized() throws Exception {
        String url = ENDPOINT + "/contacts";
        List<UUID> userIdList = List.of(USER_2_ID, UUID.randomUUID());
        String requestBody = objectMapper.writeValueAsString(userIdList);
        mvc.perform(post(url)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

}
