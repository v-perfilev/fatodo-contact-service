package com.persoff68.fatodo.builder;

import com.persoff68.fatodo.model.Request;
import com.persoff68.fatodo.web.rest.vm.RequestVM;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.UUID;

public class TestRequestVM extends RequestVM {

    @Builder
    TestRequestVM(@NotNull UUID recipientId, String message) {
        super(recipientId, message);
    }

    public static TestRequestVM.TestRequestVMBuilder defaultBuilder() {
        return TestRequestVM.builder()
                .recipientId(UUID.randomUUID());
    }

    public RequestVM toParent() {
        RequestVM requestVM = new RequestVM();
        requestVM.setRecipientId(getRecipientId());
        requestVM.setMessage(getMessage());
        return requestVM;
    }

}
