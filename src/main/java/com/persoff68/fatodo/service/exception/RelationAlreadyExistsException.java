package com.persoff68.fatodo.service.exception;

import com.persoff68.fatodo.exception.AbstractException;
import org.springframework.http.HttpStatus;

public final class RelationAlreadyExistsException extends AbstractException {
    private static final String MESSAGE = "Contact relation already exists in database";
    private static final String FEEDBACK_CODE = "contact.relation.exists";

    public RelationAlreadyExistsException() {
        super(HttpStatus.CONFLICT, MESSAGE, FEEDBACK_CODE);
    }

}
