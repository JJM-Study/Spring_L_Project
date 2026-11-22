package org.example.myproject.gis.client.sgis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SgisTokenResponse(Integer errCd, Result result) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(String accessToken, Long accessTimeout) {}
}

