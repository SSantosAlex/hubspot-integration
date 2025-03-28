package com.hubspot.utils;

import java.util.Arrays;

public enum WebhookEventType {
    CONTACT_CREATION("contact.creation"),
    UNKNOWN("unknown");

    private final String eventType;

    WebhookEventType(String eventType) {
        this.eventType = eventType;
    }

    public static WebhookEventType fromString(String eventType) {
        return Arrays.stream(values())
                .filter(e -> e.eventType.equals(eventType))
                .findFirst()
                .orElse(UNKNOWN);
    }
}
