package com.persoff68.fatodo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class RelationDTO extends AbstractAuditingDTO {

    private UUID firstUserId;

    private UUID secondUserId;

}
