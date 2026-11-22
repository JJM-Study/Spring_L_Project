package org.example.myproject;

import org.example.myproject.gis.client.sgis.*;
import org.example.myproject.gis.dto.*;
import org.example.myproject.gis.mapper.GeoCordingMapper;
import org.example.myproject.gis.service.GeoCodingService;
import org.junit.jupiter.api.Test;
import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.session.Configuration;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GeocodingServiceTest {
    @Test
    void continuesAfterFailureAndDoesNotWriteUnmatchedAddress() {
        var mapper = mock(GeoCordingMapper.class);
        var client = mock(SgisGeocodingClient.class);
        var targets = java.util.stream.LongStream.rangeClosed(1, 3).mapToObj(id -> {
            var target = new GeoCordCommonDto.GeoTarget();
            target.setLocationId(id);
            target.setAddress("address" + id);
            return target;
        }).toList();
        when(mapper.geoTargets()).thenReturn(targets);
        when(client.geocode("address1")).thenThrow(new SgisApiException("API failed"));
        when(client.geocode("address2")).thenReturn(Optional.empty());
        when(client.geocode("address3")).thenReturn(Optional.of(new Coordinates(127, 37)));
        when(mapper.updateGeoTargets(any())).thenReturn(1);
        var result = new GeoCodingService(mapper, client).geocodeTargets();
        assertEquals(3, result.total());
        assertEquals(1, result.updated());
        assertEquals(1, result.skipped());
        assertEquals(1L, result.failures().get(0).locationId());
        verify(mapper).updateGeoTargets(new GeoCordCommonDto.CoordiQuery(3L, 127, 37));
    }

    @Test
    void mapperBindsCoordinatesAsParameters() throws Exception {
        var configuration = new Configuration();
        String resource = "src/main/resources/mybatis/mappers/seller/SellerMapper.xml";
        try (var input = Files.newInputStream(Path.of(resource))) {
            new XMLMapperBuilder(input, configuration, resource, configuration.getSqlFragments()).parse();
        }
        var query = new GeoCordCommonDto.CoordiQuery(7L, 127, 37);
        var sql = configuration.getMappedStatement(GeoCordingMapper.class.getName() + ".updateGeoTargets")
                .getBoundSql(query);
        assertEquals(List.of("x", "y", "locationId"),
                sql.getParameterMappings().stream().map(p -> p.getProperty()).toList());
        assertTrue(sql.getSql().contains("longitude is null"));
    }
}
