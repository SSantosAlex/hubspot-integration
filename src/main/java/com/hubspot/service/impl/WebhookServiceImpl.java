package com.hubspot.service.impl;

import com.hubspot.Strategy.WebhookHandler;
import com.hubspot.resources.request.WebhookPayload;
import com.hubspot.service.WebhookService;
import com.hubspot.utils.WebhookEventType;
import com.hubspot.utils.WebhookEventValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WebhookServiceImpl implements WebhookService {
    private final WebhookEventValidator validator;
    private final List<WebhookHandler> handlers;

    @Override
    public Map<String, Object> processWebhook(
            String requestBody,
            String signature,
            String timestamp,
            WebhookPayload payload) {

        //if (!validator.isValid(signature, requestBody, timestamp)) {
        //    throw new SecurityException("Assinatura inválida");
        //}

        WebhookEventType eventType = payload.getEventTypeEnum();

        return handlers.stream()
                .filter(handler -> handler.canHandle(eventType))
                .findFirst()
                .orElseThrow(() -> new UnsupportedOperationException("Handler não encontrado"))
                .handle(payload);
    }
}
