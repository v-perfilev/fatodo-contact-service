package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.persoff68.fatodo.FatodoContactServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestRelation;
import com.persoff68.fatodo.builder.TestRequest;
import com.persoff68.fatodo.builder.TestRequestVM;
import com.persoff68.fatodo.client.ChatServiceClient;
import com.persoff68.fatodo.client.EventServiceClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.client.WsServiceClient;
import com.persoff68.fatodo.model.Relation;
import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.model.dto.RequestDTO;
import com.persoff68.fatodo.model.vm.RequestVM;
import com.persoff68.fatodo.repository.RelationRepository;
import com.persoff68.fatodo.repository.RequestRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoContactServiceApplication.class)
@AutoConfigureMockMvc
class RequestControllerIT {
    private static final String ENDPOINT = "/api/request";

    private static final UUID USER_1_ID = UUID.fromString("98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d");
    private static final UUID USER_2_ID = UUID.fromString("8d583dfd-acfb-4481-80e6-0b46170e2a18");
    private static final UUID USER_3_ID = UUID.fromString("5b8bfe7e-7651-4d39-a70c-22c997e376b1");

    @Autowired
    MockMvc mvc;
    @Autowired
    RelationRepository relationRepository;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserServiceClient userServiceClient;
    @MockBean
    ChatServiceClient chatServiceClient;
    @MockBean
    EventServiceClient eventServiceClient;
    @MockBean
    WsServiceClient wsServiceClient;

    @BeforeEach
    void setup() {
        Request requestOneTwo = TestRequest.defaultBuilder()
                .id(null)
                .requesterId(USER_1_ID)
                .recipientId(USER_2_ID)
                .build().toParent();

        requestRepository.save(requestOneTwo);

        Relation relationTwoThree = TestRelation.defaultBuilder()
                .id(null)
                .firstUserId(USER_2_ID)
                .secondUserId(USER_3_ID)
                .build().toParent();
        Relation relationThreeTwo = TestRelation.defaultBuilder()
                .id(null)
                .firstUserId(USER_3_ID)
                .secondUserId(USER_2_ID)
                .build().toParent();

        relationRepository.save(relationTwoThree);
        relationRepository.save(relationThreeTwo);

        when(userServiceClient.doesIdExist(any())).thenReturn(true);
    }

    @AfterEach
    void cleanup() {
        relationRepository.deleteAll();
        requestRepository.deleteAll();
    }


    @Test
    @WithCustomSecurityContext(id = "98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d")
    void testGetOutcomingRequests_ok() throws Exception {
        String url = ENDPOINT + "/outcoming";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, RequestDTO.class);
        List<RequestDTO> resultDTOList = objectMapper.readValue(resultString, listType);
        assertThat(resultDTOList).hasSize(1);
    }

    @Test
    @WithAnonymousUser
    void testGetOutcomingRequests_unauthorized() throws Exception {
        String url = ENDPOINT + "/outcoming";
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext(id = "8d583dfd-acfb-4481-80e6-0b46170e2a18")
    void testGetIncomingRequests_ok() throws Exception {
        String url = ENDPOINT + "/incoming";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, RequestDTO.class);
        List<RequestDTO> resultDTOList = objectMapper.readValue(resultString, listType);
        assertThat(resultDTOList).hasSize(1);
    }

    @Test
    @WithAnonymousUser
    void testGetIncomingRequests_unauthorized() throws Exception {
        String url = ENDPOINT + "/incoming";
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithCustomSecurityContext(id = "98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d")
    void testSendRequest_ok() throws Exception {
        RequestVM vm = TestRequestVM.defaultBuilder().recipientId(USER_3_ID).build().toParent();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isCreated());
        List<Request> requestList = requestRepository.findAllByRequesterId(USER_1_ID);
        assertThat(requestList).hasSize(2);
    }

    @Test
    @WithCustomSecurityContext(id = "98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d")
    void testSendRequest_notExist() throws Exception {
        when(userServiceClient.doesIdExist(any())).thenReturn(false);
        RequestVM vm = TestRequestVM.defaultBuilder().build().toParent();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithCustomSecurityContext(id = "98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d")
    void testSendRequest_badRequest_sameUser() throws Exception {
        RequestVM vm = TestRequestVM.defaultBuilder().recipientId(USER_1_ID).build().toParent();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithCustomSecurityContext(id = "98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d")
    void testSendRequest_conflict_requestExists() throws Exception {
        RequestVM vm = TestRequestVM.defaultBuilder().recipientId(USER_2_ID).build().toParent();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    @WithCustomSecurityContext(id = "8d583dfd-acfb-4481-80e6-0b46170e2a18")
    void testSendRequest_conflict_relationExists() throws Exception {
        RequestVM vm = TestRequestVM.defaultBuilder().recipientId(USER_3_ID).build().toParent();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isConflict());
    }

    @Test
    @WithAnonymousUser
    void testSendRequest_unauthorized() throws Exception {
        RequestVM vm = TestRequestVM.defaultBuilder().recipientId(USER_3_ID).build().toParent();
        String requestBody = objectMapper.writeValueAsString(vm);
        mvc.perform(post(ENDPOINT)
                        .contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(id = "98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d")
    void testRemoveRequest_ok() throws Exception {
        String url = ENDPOINT + "/" + USER_2_ID;
        mvc.perform(delete(url))
                .andExpect(status().isOk());
    }

    @Test
    @WithCustomSecurityContext(id = "98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d")
    void testRemoveRequest_notFound() throws Exception {
        String url = ENDPOINT + "/" + USER_3_ID;
        mvc.perform(delete(url))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    void testRemoveRequest_unauthorized() throws Exception {
        String url = ENDPOINT + "/" + USER_2_ID;
        mvc.perform(delete(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(id = "8d583dfd-acfb-4481-80e6-0b46170e2a18")
    void testAcceptRequest_ok() throws Exception {
        String url = ENDPOINT + "/" + USER_1_ID + "/accept";
        mvc.perform(put(url))
                .andExpect(status().isOk());
    }

    @Test
    @WithCustomSecurityContext(id = "8d583dfd-acfb-4481-80e6-0b46170e2a18")
    void testAcceptRequest_notFound() throws Exception {
        String url = ENDPOINT + "/" + USER_3_ID + "/accept";
        mvc.perform(put(url))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    void testAcceptRequest_unauthorized() throws Exception {
        String url = ENDPOINT + "/" + USER_1_ID + "/accept";
        mvc.perform(put(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(id = "8d583dfd-acfb-4481-80e6-0b46170e2a18")
    void testDeclineRequest_ok() throws Exception {
        String url = ENDPOINT + "/" + USER_1_ID + "/decline";
        mvc.perform(put(url))
                .andExpect(status().isOk());
    }

    @Test
    @WithCustomSecurityContext(id = "8d583dfd-acfb-4481-80e6-0b46170e2a18")
    void testDeclineRequest_notFound() throws Exception {
        String url = ENDPOINT + "/" + USER_3_ID + "/decline";
        mvc.perform(put(url))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    void testDeclineRequest_unauthorized() throws Exception {
        String url = ENDPOINT + "/" + USER_1_ID + "/decline";
        mvc.perform(put(url))
                .andExpect(status().isUnauthorized());
    }

}
