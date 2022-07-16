package com.persoff68.fatodo.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateContactEventDTO {

    private EventType type;

    private List<UUID> recipientIds;

    private UUID firstUserId;

    private UUID secondUserId;

    public enum EventType {
        CONTACT_SEND,
        CONTACT_ACCEPT,
    }

    public static CreateContactEventDTO contactSend(UUID firstUserId, UUID secondUserId) {
        List<UUID> recipientIdList = List.of(firstUserId, secondUserId);
        CreateContactEventDTO dto = new CreateContactEventDTO();
        dto.setType(EventType.CONTACT_SEND);
        dto.setRecipientIds(recipientIdList);
        dto.setFirstUserId(firstUserId);
        dto.setSecondUserId(secondUserId);
        return dto;
    }

    public static CreateContactEventDTO contactAccept(UUID firstUserId, UUID secondUserId) {
        List<UUID> recipientIdList = List.of(firstUserId, secondUserId);
        CreateContactEventDTO dto = new CreateContactEventDTO();
        dto.setType(EventType.CONTACT_ACCEPT);
        dto.setRecipientIds(recipientIdList);
        dto.setFirstUserId(firstUserId);
        dto.setSecondUserId(secondUserId);
        return dto;
    }


}
