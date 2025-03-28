package com.hubspot.client;

import com.hubspot.config.FeignConfig;
import com.hubspot.resources.request.ContactRequest;
import com.hubspot.resources.response.ContactResponse;
import com.hubspot.resources.response.OAuthResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "hubspot-api",
        url = "${hubspot.api.base-url}",
        configuration = FeignConfig.class
)
public interface HubSpotClient {

    String CONTENT_TYPE = "application/x-www-form-urlencoded;charset=utf-8";

    @PostMapping(value= "/oauth/v1/token", consumes = CONTENT_TYPE)
    OAuthResponse getAccessToken(
            @RequestParam("grant_type") String grantType,
            @RequestParam("client_id") String clientId,
            @RequestParam("client_secret") String clientSecret,
            @RequestParam("redirect_uri") String redirectUri,
            @RequestParam("code") String code
    );

    @PostMapping("/crm/v3/objects/contacts")
    ContactResponse createContact(
            @RequestHeader("Authorization") String authorization,
            @RequestBody ContactRequest request
    );

}
