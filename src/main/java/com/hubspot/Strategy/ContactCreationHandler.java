package com.hubspot.Strategy;

import com.hubspot.resources.request.WebhookPayload;
import com.hubspot.utils.WebhookEventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;

@Service
@Slf4j
public class ContactCreationHandler implements WebhookHandler {

    @Override
    public boolean canHandle(WebhookEventType eventType) {
        return eventType == WebhookEventType.CONTACT_CREATION;
    }

    @Override
    public Map<String, Object> handle(WebhookPayload payload) {
        log.info("Processando criação de contato: {}", payload.contactId());

        return Map.of(
                "status", "processed",
                "event", payload.eventType(),
                "contactId", payload.contactId(),
                "timestamp", Instant.now().toString()
        );
    }
}
