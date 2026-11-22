package org.example.myproject.gis.controller;

import org.example.myproject.gis.dto.GeocodingBatchResult;
import org.example.myproject.gis.service.GeoCodingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeoCodingController {
    private final GeoCodingService service;

    public GeoCodingController(GeoCodingService service) {
        this.service = service;
    }

    @PostMapping("/geocoding/sgis")
    public GeocodingBatchResult geocode() {
        return service.geocodeTargets();
    }
}
