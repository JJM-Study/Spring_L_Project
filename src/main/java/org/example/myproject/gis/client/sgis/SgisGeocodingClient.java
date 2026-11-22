package org.example.myproject.gis.client.sgis;

import org.example.myproject.gis.client.sgis.dto.SgisGeocodingResponse;
import org.example.myproject.gis.dto.Coordinates;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import java.util.Map;
import java.util.Optional;

@Component
public class SgisGeocodingClient {
    private final RestClient client;
    private final SgisTokenProvider tokens;

    public SgisGeocodingClient(@Qualifier("sgisRestClient") RestClient client,
            SgisTokenProvider tokens) {
        this.client = client;
        this.tokens = tokens;
    }

    public Optional<Coordinates> geocode(String address) {
        if (address == null || address.isBlank()) {
            throw new IllegalArgumentException("Address is required");
        }
        String token = tokens.getAccessToken();
        SgisGeocodingResponse response;
        try {
            response = client.get().uri(builder -> builder
                    .path("/OpenAPI3/addr/geocodewgs84.json")
                    .queryParam("accessToken", "{token}")
                    .queryParam("address", "{address}")
                    .queryParam("resultcount", 1)
                    .build(Map.of("token", token, "address", address)))
                    .retrieve().body(SgisGeocodingResponse.class);
        } catch (RestClientException e) {
            throw new SgisApiException("SGIS geocoding request failed");
        }
        if (response != null && Integer.valueOf(-100).equals(response.errCd())) {
            return Optional.empty();
        }
        if (response == null || !Integer.valueOf(0).equals(response.errCd())
                || response.result() == null || response.result().resultdata() == null) {
            throw new SgisApiException("SGIS geocoding response is invalid");
        }
        // Do not persist coordinates from an incomplete address match.
        if ("1".equals(response.result().matching()) || response.result().resultdata().isEmpty()) {
            return Optional.empty();
        }
        var point = response.result().resultdata().get(0);
        if (point == null || point.x() == null || point.y() == null) {
            throw new SgisApiException("SGIS coordinates are missing");
        }
        try {
            return Optional.of(new Coordinates(point.x(), point.y()));
        } catch (IllegalArgumentException e) {
            throw new SgisApiException("SGIS coordinates are invalid");
        }
    }
}

