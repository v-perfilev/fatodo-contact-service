package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.dto.RequestDTO;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class TestRequestDTO extends RequestDTO {

    @Builder
    public TestRequestDTO(UUID id, @NotNull UUID requesterId, @NotNull UUID recipientId) {
        super(requesterId, recipientId);
        this.id = id;
    }

    public static TestRequestDTOBuilder defaultBuilder() {
        return TestRequestDTO.builder()
                .id(UUID.randomUUID())
                .requesterId(UUID.randomUUID())
                .recipientId(UUID.randomUUID());
    }

}
