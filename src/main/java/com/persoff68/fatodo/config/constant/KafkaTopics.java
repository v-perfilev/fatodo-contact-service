package com.persoff68.fatodo.config.constant;

import lombok.Getter;

public enum KafkaTopics {
    EVENT_ADD("event_add"),
    EVENT_DELETE("event_delete"),
    WS_CLEAR("ws_clear"),
    WS_CONTACT("ws_contact");

    @Getter
    private final String value;

    KafkaTopics(String value) {
        this.value = value;
    }

}
