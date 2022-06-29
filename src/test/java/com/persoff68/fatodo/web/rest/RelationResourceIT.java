package com.persoff68.fatodo.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.persoff68.fatodo.FatodoContactServiceApplication;
import com.persoff68.fatodo.annotation.WithCustomSecurityContext;
import com.persoff68.fatodo.builder.TestRelation;
import com.persoff68.fatodo.model.Relation;
import com.persoff68.fatodo.model.dto.RelationDTO;
import com.persoff68.fatodo.repository.RelationRepository;
import com.persoff68.fatodo.repository.RequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = FatodoContactServiceApplication.class)
class RelationResourceIT {
    private static final String ENDPOINT = "/api/relations";

    private static final UUID USER_1_ID = UUID.fromString("98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d");
    private static final UUID USER_2_ID = UUID.fromString("8d583dfd-acfb-4481-80e6-0b46170e2a18");
    private static final UUID USER_3_ID = UUID.fromString("5b8bfe7e-7651-4d39-a70c-22c997e376b1");

    @Autowired
    WebApplicationContext context;
    @Autowired
    RelationRepository relationRepository;
    @Autowired
    RequestRepository requestRepository;
    @Autowired
    ObjectMapper objectMapper;

    MockMvc mvc;

    @BeforeEach
    void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();

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
        Relation relationOneThree = TestRelation.defaultBuilder()
                .id(null)
                .firstUserId(USER_1_ID)
                .secondUserId(USER_3_ID)
                .build().toParent();
        Relation relationThreeOne = TestRelation.defaultBuilder()
                .id(null)
                .firstUserId(USER_3_ID)
                .secondUserId(USER_1_ID)
                .build().toParent();

        relationRepository.deleteAll();
        relationRepository.save(relationOneTwo);
        relationRepository.save(relationTwoOne);
        relationRepository.save(relationOneThree);
        relationRepository.save(relationThreeOne);
    }

    @Test
    @WithCustomSecurityContext(id = "98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d")
    void testGetRelations_ok() throws Exception {
        ResultActions resultActions = mvc.perform(get(ENDPOINT))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, RelationDTO.class);
        List<RelationDTO> resultDTOList = objectMapper.readValue(resultString, listType);
        assertThat(resultDTOList).hasSize(2);
    }

    @Test
    @WithAnonymousUser
    void testGetRelations_unauthorized() throws Exception {
        mvc.perform(get(ENDPOINT))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(id = "8d583dfd-acfb-4481-80e6-0b46170e2a18")
    void testGetCommonRelationsByUsers_ok() throws Exception {
        String url = ENDPOINT + "/" + USER_3_ID + "/user";
        ResultActions resultActions = mvc.perform(get(url))
                .andExpect(status().isOk());
        String resultString = resultActions.andReturn().getResponse().getContentAsString();
        CollectionType listType = objectMapper.getTypeFactory().constructCollectionType(List.class, RelationDTO.class);
        List<RelationDTO> resultDTOList = objectMapper.readValue(resultString, listType);
        assertThat(resultDTOList).hasSize(1);
    }

    @Test
    @WithAnonymousUser
    void testGetCommonRelationsByUsers_unauthorized() throws Exception {
        String url = ENDPOINT + "/" + USER_3_ID + "/user";
        mvc.perform(get(url))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithCustomSecurityContext(id = "98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d")
    void testRemoveRelation_ok() throws Exception {
        String url = ENDPOINT + "/" + USER_3_ID;
        mvc.perform(delete(url))
                .andExpect(status().isOk());

        List<Relation> relationList = relationRepository.findAllByFirstUserId(USER_1_ID);
        assertThat(relationList).hasSize(1);
    }

    @Test
    @WithCustomSecurityContext(id = "98a4f736-70c2-4c7d-b75b-f7a5ae7bbe8d")
    void testRemoveRelation_notFound() throws Exception {
        String url = ENDPOINT + "/" + UUID.randomUUID();
        mvc.perform(delete(url))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithAnonymousUser
    void testRemoveRelation_unauthorized() throws Exception {
        String url = ENDPOINT + "/" + USER_3_ID;
        mvc.perform(delete(url))
                .andExpect(status().isUnauthorized());
    }

}
