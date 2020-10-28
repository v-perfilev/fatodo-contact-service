package com.persoff68.fatodo.service.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public final class RequestAlreadyExistsException extends AbstractException {
    private static final String MESSAGE = "Contact request already exists in database";
    private static final String FEEDBACK_CODE = "contact.request.exists";

    public RequestAlreadyExistsException() {
        super(HttpStatus.CONFLICT, MESSAGE, FEEDBACK_CODE);
    }

}
