package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.builder.TestRelation;
import com.persoff68.fatodo.builder.TestRequest;
import com.persoff68.fatodo.client.ChatServiceClient;
import com.persoff68.fatodo.client.EventServiceClient;
import com.persoff68.fatodo.client.UserServiceClient;
import com.persoff68.fatodo.model.Relation;
import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.repository.RelationRepository;
import com.persoff68.fatodo.repository.RequestRepository;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.verifier.messaging.boot.AutoConfigureMessageVerifier;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMessageVerifier
public abstract class ContractBase {
    private static final UUID USER_1_ID = UUID.fromString("8f9a7cae-73c8-4ad6-b135-5bd109b51d2e");
    private static final UUID USER_2_ID = UUID.fromString("8d583dfd-acfb-4481-80e6-0b46170e2a18");
    private static final UUID USER_3_ID = UUID.fromString("5b8bfe7e-7651-4d39-a70c-22c997e376b1");
    private static final UUID USER_4_ID = UUID.fromString("6e7fb1ec-dd71-4ab9-9b11-b632c4ccbf18");
    private static final UUID USER_5_ID = UUID.fromString("0554562b-1ef5-4311-9462-6aa3c40b5627");

    @Autowired
    WebApplicationContext context;
    @Autowired
    RelationRepository relationRepository;
    @Autowired
    RequestRepository requestRepository;
    @MockBean
    UserServiceClient userServiceClient;
    @MockBean
    ChatServiceClient chatServiceClient;
    @MockBean
    EventServiceClient eventServiceClient;

    @BeforeEach
    public void setup() {
        RestAssuredMockMvc.webAppContextSetup(context);

        Request requestOneTwo = TestRequest.defaultBuilder()
                .id(null)
                .requesterId(USER_1_ID)
                .recipientId(USER_2_ID)
                .build().toParent();

        Request requestThreeOne = TestRequest.defaultBuilder()
                .id(null)
                .requesterId(USER_3_ID)
                .recipientId(USER_1_ID)
                .build().toParent();

        requestRepository.save(requestOneTwo);
        requestRepository.save(requestThreeOne);

        Relation relationOneFour = TestRelation.defaultBuilder()
                .id(null)
                .firstUserId(USER_1_ID)
                .secondUserId(USER_4_ID)
                .build().toParent();
        Relation relationFourOne = TestRelation.defaultBuilder()
                .id(null)
                .firstUserId(USER_4_ID)
                .secondUserId(USER_1_ID)
                .build().toParent();

        Relation relationFiveFour = TestRelation.defaultBuilder()
                .id(null)
                .firstUserId(USER_5_ID)
                .secondUserId(USER_4_ID)
                .build().toParent();
        Relation relationFourFive = TestRelation.defaultBuilder()
                .id(null)
                .firstUserId(USER_4_ID)
                .secondUserId(USER_5_ID)
                .build().toParent();

        relationRepository.save(relationOneFour);
        relationRepository.save(relationFourOne);

        relationRepository.save(relationFiveFour);
        relationRepository.save(relationFourFive);

        when(userServiceClient.doesIdExist(any())).thenReturn(true);
        doNothing().when(chatServiceClient).sendDirect(any(), any());
        doNothing().when(eventServiceClient).addContactEvent(any());
        doNothing().when(eventServiceClient).deleteContactEvents(any());
    }

    @AfterEach
    void cleanup() {
        relationRepository.deleteAll();
        requestRepository.deleteAll();
    }

}
