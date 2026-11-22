package org.example.myproject.gis.client.sgis;

import org.example.myproject.gis.client.sgis.dto.SgisTokenResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import java.time.Instant;
import java.util.Map;

@Component
public class SgisTokenProvider {
    private final RestClient client;
    private final String serviceId;
    private final String securityKey;
    private String token;
    private Instant expiresAt = Instant.EPOCH;

    public SgisTokenProvider(@Qualifier("sgisRestClient") RestClient client,
            @Value("${sgis.service-id}") String serviceId,
            @Value("${sgis.security-key}") String securityKey) {
        this.client = client;
        this.serviceId = serviceId;
        this.securityKey = securityKey;
    }

    public synchronized String getAccessToken() {
        if (token != null && Instant.now().plusSeconds(60).isBefore(expiresAt)) {
            return token;
        }
        if (serviceId.isBlank() || securityKey.isBlank()) {
            throw new SgisApiException("SGIS credentials are missing");
        }
        SgisTokenResponse response;
        try {
            response = client.get().uri(builder -> builder
                    .path("/OpenAPI3/auth/authentication.json")
                    .queryParam("consumer_key", "{key}")
                    .queryParam("consumer_secret", "{secret}")
                    .build(Map.of("key", serviceId, "secret", securityKey)))
                    .retrieve().body(SgisTokenResponse.class);
        } catch (RestClientException e) {
            String type = e.getCause() == null ? e.getClass().getSimpleName()
                    : e.getCause().getClass().getSimpleName();
            throw new SgisApiException("SGIS authentication request failed (" + type + ")");
        }
        if (response == null || !Integer.valueOf(0).equals(response.errCd())
                || response.result() == null || response.result().accessToken() == null
                || response.result().accessToken().isBlank()
                || response.result().accessTimeout() == null) {
            throw new SgisApiException("SGIS authentication response is invalid");
        }
        long timeout = response.result().accessTimeout();
        // Documentation says seconds; published examples use epoch milliseconds.
        Instant expiry = timeout >= 100_000_000_000L
                ? Instant.ofEpochMilli(timeout) : Instant.ofEpochSecond(timeout);
        if (!Instant.now().plusSeconds(60).isBefore(expiry)) {
            throw new SgisApiException("SGIS returned an expired token");
        }
        token = response.result().accessToken();
        expiresAt = expiry;
        return token;
    }
}

