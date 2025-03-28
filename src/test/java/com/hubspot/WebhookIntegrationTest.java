package com.hubspot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebhookIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldProcessValidWebhook() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-HubSpot-Signature-v3", "valid_signature");
        headers.set("X-HubSpot-Request-Timestamp", String.valueOf(System.currentTimeMillis()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = """
            {
              "objectId": 12345,
              "eventType": "contact.creation",
              "properties": {
                "email": {"value": "test@example.com"}
              }
            }""";

        ResponseEntity<Map> response = restTemplate.exchange(
                "/webhooks",
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Map.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody().get("status"));
    }
}