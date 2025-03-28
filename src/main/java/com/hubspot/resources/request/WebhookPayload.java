package com.hubspot.resources.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hubspot.dto.HubSpotPropertyValue;
import com.hubspot.utils.WebhookEventType;

import java.util.Map;

public record WebhookPayload(
        @JsonProperty("objectId") Long contactId,
        @JsonProperty("eventType") String eventType,
        @JsonProperty("properties") Map<String, HubSpotPropertyValue> properties
) {
    public WebhookEventType getEventTypeEnum() {
        return WebhookEventType.fromString(eventType);
    }
}