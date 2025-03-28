package com.hubspot.service;

import com.hubspot.resources.response.OAuthResponse;

public interface OAuthService {

    OAuthResponse getAccessToken(String code);
    String generateAuthorizationUrl();
}
