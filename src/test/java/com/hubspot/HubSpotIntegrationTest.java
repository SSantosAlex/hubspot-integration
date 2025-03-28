package com.hubspot;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HubSpotIntegrationTest extends SpringIntegrationTest {

	@Value("${hubspot.api.base-url}")
	private String hubspotApiUrl;

	@Test
	void testCreateContact() {

		wireMockServer.stubFor(post(urlEqualTo("/crm/v3/objects/contacts"))
				.withHeader("Authorization", equalTo("Bearer MOCK_ACCESS_TOKEN"))
				.withHeader("Content-Type", equalTo("application/json"))
				.willReturn(aResponse()
						.withStatus(HttpStatus.CREATED.value())
						.withHeader("Content-Type", "application/json")
						.withBody("{\"id\": \"12345\"}")
				));

		String url = wireMockServer.baseUrl() + "/crm/v3/objects/contacts";
		String requestBody = "{\"properties\": {\"email\": \"test@example.com\", \"firstname\": \"John\", \"lastname\": \"Doe\"}}";

		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "Bearer MOCK_ACCESS_TOKEN");
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<>(requestBody, headers);

		ResponseEntity<String> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				request,
				String.class
		);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
		assertThat(response.getBody()).contains("\"id\": \"12345\"");

	}
}