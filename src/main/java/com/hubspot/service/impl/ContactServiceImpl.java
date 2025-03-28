package com.hubspot.service.impl;

import com.hubspot.client.HubSpotClient;
import com.hubspot.resources.request.ContactRequest;
import com.hubspot.resources.response.ContactResponse;
import com.hubspot.resources.response.OAuthResponse;
import com.hubspot.service.ContactService;
import com.hubspot.service.OAuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactServiceImpl implements ContactService {
    private final HubSpotClient hubSpotClient;
    private final OAuthService oAuthService;

    @Override
    public ContactResponse createContact(ContactRequest request) {
        OAuthResponse response = oAuthService.getAccessToken(null);
        log.info("token={}", response.accessToken());
        return hubSpotClient.createContact("Bearer " + response.accessToken(), request);
    }

}
