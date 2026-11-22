package org.example.myproject.gis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class GeoCordCommonDto {

    @Data
    public static class GeoTarget
    {
        private Long locationId;
        private String address;
    }

    @Data
    public static class CoordiQuery
    {
        private final Long locationId;
        private final double x;
        private final double y;
    }


}
