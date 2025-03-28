package com.hubspot.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hubspot.client.HubSpotClient;
import com.hubspot.exception.OAuthException;
import com.hubspot.resources.response.OAuthResponse;
import com.hubspot.service.OAuthService;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.UUID;

@Service
@Slf4j
public class OAuthServiceImpl implements OAuthService {

    private static final String AUTHORIZATION_URL = "https://app.hubspot.com/oauth/authorize";
    private static final String GRANT_TYPE = "authorization_code";
    private static final String OAUTH_ERROR_KEY = "hubspot_oauth_error";

    private final HubSpotClient hubSpotClient;
    private final String clientId;
    private final String clientSecret;
    private final String redirectUri;
    private final String scope;
    private final ObjectMapper objectMapper;

    public OAuthServiceImpl(
            HubSpotClient hubSpotClient,
            @Value("${hubspot.client.id}") String clientId,
            @Value("${hubspot.client.secret}") String clientSecret,
            @Value("${hubspot.redirect.uri}") String redirectUri,
            @Value("${hubspot.scope}") String scope,
            ObjectMapper objectMapper) {
        this.hubSpotClient = hubSpotClient;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUri = redirectUri;
        this.scope = scope;
        this.objectMapper = objectMapper;
    }

    @Override
    public String generateAuthorizationUrl() {
        return UriComponentsBuilder.fromUriString(AUTHORIZATION_URL)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", scope)
                .queryParam("state", generateAntiForgeryToken())
                .build()
                .toUriString();
    }
    @Cacheable(value = "oauthTokens", key = "'accessToken'")
    @Override
    public OAuthResponse getAccessToken(String code) {
        try {
            return hubSpotClient.getAccessToken(
                    GRANT_TYPE,
                    clientId,
                    clientSecret,
                    redirectUri,
                    code
            );
        } catch (FeignException e) {
            log.error("Falha na comunicação com o HubSpot. Status: {}, Body: {}",
                    e.status(), e.contentUTF8());

            throw new OAuthException(
                    OAUTH_ERROR_KEY,
                    parseErrorMessage(e.contentUTF8()),
                    "HubSpot",
                    e.status()
            );
        }
    }

    private String generateAntiForgeryToken() {
        return UUID.randomUUID().toString();
    }

    private String parseErrorMessage(String errorResponse) {
        try {
            JsonNode node = objectMapper.readTree(errorResponse);
            return node.path("message").asText("Falha desconhecida na autenticação");
        } catch (IOException ex) {
            log.warn("Falha ao parsear erro do HubSpot", ex);
            return "Erro ao processar resposta do HubSpot";
        }
    }
}