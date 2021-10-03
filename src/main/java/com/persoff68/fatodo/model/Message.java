package com.persoff68.fatodo.model;

import lombok.Data;

import java.util.UUID;

@Data
public class Message {
    private String text;
    private UUID referenceId;
}
