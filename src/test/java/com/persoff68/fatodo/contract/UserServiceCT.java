package com.persoff68.fatodo.contract;

import com.persoff68.fatodo.client.UserServiceClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.stubrunner.spring.AutoConfigureStubRunner;
import org.springframework.cloud.contract.stubrunner.spring.StubRunnerProperties;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureStubRunner(ids = {"com.persoff68.fatodo:userservice:+:stubs"},
        stubsMode = StubRunnerProperties.StubsMode.REMOTE)
class UserServiceCT {

    @Autowired
    UserServiceClient userServiceClient;

    @Test
    void testDoesIdExist() {
        boolean doesIdExist = userServiceClient.doesIdExist(UUID.randomUUID());
        assertThat(doesIdExist).isFalse();
    }

}
