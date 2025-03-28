package com.hubspot.service;

import com.hubspot.resources.request.WebhookPayload;

import java.util.Map;

public interface WebhookService {

    Map<String, Object> processWebhook(
            String requestBody,
            String signature,
            String timestamp,
            WebhookPayload payload
    );
}
