package org.example.myproject.gis.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.RequiredArgsConstructor;


import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OutputGISDto {

    //@JsonProperty("center_lat")
    private Double centerLat;

    //@JsonProperty("center_lon")
    private Double centerLon;

    //@JsonProperty("nearby_nodes")
    private List<NodeInfo> nearbyNodes;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    static class NodeInfo {
        //@JsonProperty("node_id")
        private int nodeId;

        //@JsonProperty("distance_km")
        private Double distanceKm;

        //@JsonProperty("lat")
        private Double lat;

        //@JsonProperty("lon")
        private Double lon;

    }


}
