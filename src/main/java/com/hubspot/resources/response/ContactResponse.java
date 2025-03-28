package com.hubspot.resources.response;

import java.time.Instant;
import java.util.Map;

public record ContactResponse(String id, Map<String, String> properties, Instant createdAt, Instant updatedAt) {}
