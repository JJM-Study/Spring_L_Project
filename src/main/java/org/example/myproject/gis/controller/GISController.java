package org.example.myproject.gis.controller;

import org.example.myproject.gis.client.SpatialEngineClient;
import org.example.myproject.gis.dto.InputGISDto;
import org.example.myproject.gis.dto.OutputGISDto;
import org.example.myproject.gis.service.SpatialLocationExportService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestClientResponseException;
import java.util.Map;

@RestController
public class GISController {
    private final SpatialEngineClient engine;
    private final SpatialLocationExportService locations;

    public GISController(SpatialEngineClient engine, SpatialLocationExportService locations) {
        this.engine = engine;
        this.locations = locations;
    }

    @PutMapping("/api/spatial/locations")
    public SpatialEngineClient.IndexStatus syncLocations() {
        return engine.replace(locations.findValidLocations());
    }

    @PostMapping("/nearby")
    public OutputGISDto nearby(@RequestBody InputGISDto query) {
        OutputGISDto result = engine.nearby(query);
        locations.attachSellingLocations(result);
        return result;
    }

    @ExceptionHandler(RestClientResponseException.class)
    public ResponseEntity<byte[]> engineRejected(RestClientResponseException e) {
        return ResponseEntity.status(e.getStatusCode()).contentType(MediaType.APPLICATION_JSON)
                .body(e.getResponseBodyAsByteArray());
    }

    @ExceptionHandler(RestClientException.class)
    public ResponseEntity<?> engineUnavailable(RestClientException e) {
        return ResponseEntity.status(502).body(Map.of("code", "ENGINE_UNAVAILABLE"));
    }
}
