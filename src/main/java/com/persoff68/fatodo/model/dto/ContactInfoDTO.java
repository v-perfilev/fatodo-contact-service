package com.persoff68.fatodo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContactInfoDTO {

    private int relationCount;
    private int outcomingRequestCount;
    private int incomingRequestCount;

}
