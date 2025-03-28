package com.hubspot;

import com.hubspot.utils.WebhookEventValidator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class HubSpotSignatureValidatorTest {

    @Autowired
    private WebhookEventValidator validator;

    private final String secret = "seu_client_secret";
    private final String body = "{\"event\":\"contact.creation\"}";
    private final String timestamp = String.valueOf(System.currentTimeMillis());

    @Test
    void shouldValidateCorrectSignature() {
        String validSignature = validator.computeSignature(body, timestamp);
        assertTrue(validator.isValid(validSignature, body, timestamp));
    }

    @Test
    void shouldRejectInvalidSignature() {
        assertFalse(validator.isValid("assinatura_invalida", body, timestamp));
    }

    @Test
    void shouldRejectExpiredTimestamp() {
        String oldTimestamp = String.valueOf(System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(6));
        assertThrows(SecurityException.class, () ->
                validator.isValid("any_signature", body, oldTimestamp));
    }
}