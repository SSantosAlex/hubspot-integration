package com.hubspot.Strategy;

import com.hubspot.resources.request.WebhookPayload;
import com.hubspot.utils.WebhookEventType;

import java.util.Map;

public interface WebhookHandler {

    boolean canHandle(WebhookEventType eventType);
    Map<String, Object> handle(WebhookPayload payload);
}
