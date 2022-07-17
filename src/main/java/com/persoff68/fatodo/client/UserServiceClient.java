package com.persoff68.fatodo.client;

import com.persoff68.fatodo.client.configuration.FeignAuthConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service", primary = false,
        configuration = {FeignAuthConfiguration.class},
        qualifiers = {"feignUserServiceClient"})
public interface UserServiceClient {

    @GetMapping(value = "/api/check/id/{id}")
    boolean doesIdExist(@PathVariable UUID id);

}

