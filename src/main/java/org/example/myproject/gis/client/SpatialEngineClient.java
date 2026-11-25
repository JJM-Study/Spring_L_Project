package org.example.myproject.gis.client;

import org.example.myproject.config.properties.EngineApiProperties;
import org.example.myproject.gis.dto.InputGISDto;
import org.example.myproject.gis.dto.OutputGISDto;
import org.example.myproject.gis.dto.SpatialLocationExportDto;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import java.time.Instant;
import java.util.List;

@Component
public class SpatialEngineClient {
    private final RestClient http;
    private final EngineApiProperties settings;

    public SpatialEngineClient(RestClient.Builder builder, EngineApiProperties settings) {
        this.settings = settings;
        this.http = builder.baseUrl(settings.getBaseUrl() + settings.getSpatialPath()).build();
    }

    public IndexStatus replace(List<SpatialLocationExportDto> locations) {
        checkConfiguration();
        return requireBody(http.put().uri("/clients/{id}/locations", settings.getClientId())
                .header("X-API-Key", settings.getApiKey())
                .body(new Locations(locations)).retrieve().body(IndexStatus.class));
    }

    public OutputGISDto nearby(InputGISDto query) {
        checkConfiguration();
        return requireBody(http.post().uri("/clients/{id}/nearby", settings.getClientId())
                .header("X-API-Key", settings.getApiKey())
                .body(query).retrieve().body(OutputGISDto.class));
    }

    private void checkConfiguration() {
        if (settings.getClientId() == null || settings.getClientId() < 1
                || settings.getApiKey() == null || settings.getApiKey().isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Spatial engine credentials are missing");
        }
    }

    private static <T> T requireBody(T body) {
        if (body == null) throw new RestClientException("Empty response from spatial engine");
        return body;
    }

    private record Locations(List<SpatialLocationExportDto> locations) {}
    public record IndexStatus(long clientId, String status, long dataVersion, int locationCount, Instant loadedAt) {}
}
