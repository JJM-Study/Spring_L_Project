package org.example.myproject.gis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeoSGISResponseDto
{
    private coordinate resultdata;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class coordinate{
        private double x;
        private double y;
    }
}