package org.example.myproject.gis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;

import javax.xml.stream.Location;
import java.util.List;

@RequiredArgsConstructor
public class OutputGISDto {

    @JsonProperty("my_location")
    private MyLocation myLocation;

    @JsonProperty("nearby_locations")
    private List<NearbyLocation> nearbyLocations;

    static class MyLocation {

        @JsonProperty("lat")
        private Double lat;
        @JsonProperty("lon")
        private Double lon;

    }

    static class NearbyLocation {
        @JsonProperty("node_id")
        private int nodeId;

        @JsonProperty("distance_km")
        private Double distanceKm;

        @JsonProperty("lat")
        private Double lat;

        @JsonProperty("lon")
        private Double lon;

    }


}
