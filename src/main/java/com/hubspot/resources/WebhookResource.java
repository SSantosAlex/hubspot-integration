package com.hubspot.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.resources.request.WebhookPayload;
import com.hubspot.service.impl.WebhookServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
public class WebhookResource{
    private final WebhookServiceImpl webhookService;
    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<Map<String, Object>> handleWebhook(
            @RequestHeader("X-HubSpot-Signature-v3") String signature,
            @RequestHeader("X-HubSpot-Request-Timestamp") String timestamp,
            @RequestBody String rawRequestBody) {

        try {
            WebhookPayload payload = objectMapper.readValue(rawRequestBody, WebhookPayload.class);

            Map<String, Object> response = webhookService.processWebhook(
                    rawRequestBody,
                    signature,
                    timestamp,
                    payload
            );

            log.info("c={} m=handleWebhook response={}",getClass().getSimpleName(), response);

            return ResponseEntity.ok(response);

        } catch (SecurityException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", "Requisição inválida"));
        }
    }
}
