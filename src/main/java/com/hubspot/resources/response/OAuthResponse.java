package com.hubspot.resources.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record OAuthResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("expires_in") Integer expiresIn
) {}
