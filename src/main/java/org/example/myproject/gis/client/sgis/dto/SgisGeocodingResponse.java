package org.example.myproject.gis.client.sgis.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonAlias;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record SgisGeocodingResponse(Integer errCd, Result result) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Result(String matching, List<Point> resultdata) {}
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Point(@JsonAlias("X") Double x, @JsonAlias("Y") Double y) {}
}

