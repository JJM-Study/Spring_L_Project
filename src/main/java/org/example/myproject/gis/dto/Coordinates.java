package org.example.myproject.gis.dto;

public record Coordinates(double longitude, double latitude) {
    public Coordinates {
        if (!Double.isFinite(longitude) || !Double.isFinite(latitude)
                || Math.abs(longitude) > 180 || Math.abs(latitude) > 90) {
            throw new IllegalArgumentException("Invalid WGS84 coordinates");
        }
    }
}

