package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.Request;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class TestRequest extends Request {

    @Builder
    TestRequest(UUID id, @NotNull UUID requesterId, @NotNull UUID recipientId,String message) {
        super(requesterId, recipientId, message);
        this.id = id;
    }

    public static TestRequestBuilder defaultBuilder() {
        return TestRequest.builder()
                .id(UUID.randomUUID())
                .requesterId(UUID.randomUUID())
                .recipientId(UUID.randomUUID());
    }

    public Request toParent() {
        Request request = new Request();
        request.setId(getId());
        request.setRequesterId(getRequesterId());
        request.setRecipientId(getRecipientId());
        request.setMessage(getMessage());
        return request;
    }

}
