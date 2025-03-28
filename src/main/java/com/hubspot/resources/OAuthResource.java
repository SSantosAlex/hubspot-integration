package com.hubspot.resources;

import com.hubspot.resources.response.OAuthResponse;
import com.hubspot.service.OAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class OAuthResource {
    private final OAuthService oAuthService;

    @GetMapping("/authorize")
    public ResponseEntity<String> getAuthorizationUrl() {
        return ResponseEntity.ok(oAuthService.generateAuthorizationUrl());
    }

    @GetMapping("/callback")
    public ResponseEntity<OAuthResponse> callback(@RequestParam("code") String code) {
        return ResponseEntity.ok(oAuthService.getAccessToken(code));
    }
}