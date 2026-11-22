package org.example.myproject.gis.dto;

import java.util.List;

public record GeocodingBatchResult(int total, int updated, int skipped, List<Failure> failures) {
    public record Failure(Long locationId, String reason) {}
}

