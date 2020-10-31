package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.dto.RelationDTO;
import lombok.Builder;

import java.util.UUID;

public class TestRelationDTO extends RelationDTO {

    @Builder
    public TestRelationDTO(UUID id, UUID firstUserId, UUID secondUserId) {
        super(firstUserId, secondUserId);
        this.id = id;
    }

    public static TestRelationDTOBuilder defaultBuilder() {
        return TestRelationDTO.builder()
                .id(UUID.randomUUID())
                .firstUserId(UUID.randomUUID())
                .secondUserId(UUID.randomUUID());
    }

}
