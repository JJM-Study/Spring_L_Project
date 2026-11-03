package org.example.myproject.gis.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;


import java.util.List;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class OutputGISDto {

    //@JsonProperty("center_lon")
    private Double centerLon;

    //@JsonProperty("center_lat")
    private Double centerLat;

    //@JsonProperty("nearby_nodes")
    private List<NodeInfo> nearbyNodes;
//
//    private List<ShopInfo> shopInfo;

    @Data
    @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
    public static class NodeInfo {
        //@JsonProperty("node_id")
        private int nodeId;

        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private List<String> externalId;

        //@JsonProperty("distance_km")
        private Double distance;

        //@JsonProperty("lon")
        private Double lon;

        //@JsonProperty("lat")
        private Double lat;

        private List<ShopInfo> shopInfo;


        // 2026/10/22 추가
        @Data
        @JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
        public static class ShopInfo {
            private String shopId;
            private String shopName;
            private String categoryLarge;
            private String categoryMid;
            private String roadAddress;


        }


    }



}
