package com.hubspot.resources.request;

import java.util.Map;

public record ContactRequest(Map<String, String> properties) {}