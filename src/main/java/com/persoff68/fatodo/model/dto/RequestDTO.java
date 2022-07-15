package com.persoff68.fatodo.model.dto;

import com.persoff68.fatodo.config.constant.AppConstants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RequestDTO extends AbstractAuditingDTO {
    @Serial
    private static final long serialVersionUID = AppConstants.SERIAL_VERSION_UID;

    private UUID requesterId;
    private UUID recipientId;
    private String message;

}
