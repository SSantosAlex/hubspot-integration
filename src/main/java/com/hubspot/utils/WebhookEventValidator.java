package com.hubspot.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.util.concurrent.TimeUnit;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
@RequiredArgsConstructor
public class WebhookEventValidator {

    @Value("${hubspot.client.secret}")
    private String clientSecret;



    public boolean isValid(String signature, String requestBody, String timestamp) {

        if (isTimestampExpired(timestamp)) {
            throw new SecurityException("Timestamp expirado");
        }

        String computedSignature = computeSignature(requestBody, timestamp);

        return secureCompare(signature, computedSignature);
    }

    public String computeSignature(String requestBody, String timestamp) {
        try {
            String sourceString = requestBody + timestamp;
            Mac hmac = Mac.getInstance("HmacSHA256");
            hmac.init(new SecretKeySpec(clientSecret.getBytes(), "HmacSHA256"));
            byte[] hash = hmac.doFinal(sourceString.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new SecurityException("Falha ao gerar assinatura", e);
        }
    }

    private boolean secureCompare(String a, String b) {
        return MessageDigest.isEqual(a.getBytes(), b.getBytes());
    }

    private boolean isTimestampExpired(String timestamp) {
        long requestTime = Long.parseLong(timestamp);
        long currentTime = System.currentTimeMillis();
        return (currentTime - requestTime) > TimeUnit.MINUTES.toMillis(5);
    }
}
