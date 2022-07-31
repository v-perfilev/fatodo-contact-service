package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.dto.RequestEventDTO;
import lombok.Builder;

import java.util.UUID;

public class TestRequestEventDTO extends RequestEventDTO {

    @Builder
    TestRequestEventDTO(UUID requesterId, UUID recipientId) {
        super();
        super.setRequesterId(requesterId);
        super.setRecipientId(recipientId);
    }

    public static TestRequestEventDTOBuilder defaultBuilder() {
        return TestRequestEventDTO.builder()
                .requesterId(UUID.randomUUID())
                .recipientId(UUID.randomUUID());
    }

    public RequestEventDTO toParent() {
        RequestEventDTO dto = new RequestEventDTO();
        dto.setRequesterId(getRequesterId());
        dto.setRecipientId(getRecipientId());
        return dto;
    }

}

