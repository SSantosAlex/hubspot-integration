package com.hubspot.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HubSpotPropertyValue(
        @JsonProperty("value") Object value,
        @JsonProperty("timestamp") Long timestamp,
        @JsonProperty("source") String source,
        @JsonProperty("sourceId") String sourceId,
        @JsonProperty("sourceLabel") String sourceLabel,
        @JsonProperty("updatedByUserId") Long updatedBy
) {}
