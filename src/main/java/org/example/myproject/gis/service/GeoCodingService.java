package org.example.myproject.gis.service;

import org.example.myproject.gis.client.sgis.SgisGeocodingClient;
import org.example.myproject.gis.client.sgis.SgisApiException;
import org.example.myproject.gis.dto.GeoCordCommonDto;
import org.example.myproject.gis.dto.GeocodingBatchResult;
import org.example.myproject.gis.mapper.GeoCordingMapper;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class GeoCodingService {
    private final GeoCordingMapper mapper;
    private final SgisGeocodingClient client;

    public GeoCodingService(GeoCordingMapper mapper, SgisGeocodingClient client) {
        this.mapper = mapper;
        this.client = client;
    }

    // External calls are outside a long-running DB transaction.
    public GeocodingBatchResult geocodeTargets() {
        var targets = mapper.geoTargets();
        int updated = 0;
        int skipped = 0;
        var failures = new ArrayList<GeocodingBatchResult.Failure>();
        for (var target : targets) {
            try {
                var result = client.geocode(target.getAddress());
                if (result.isEmpty()) {
                    skipped++;
                    continue;
                }
                var coordinates = result.get();
                int rows = mapper.updateGeoTargets(new GeoCordCommonDto.CoordiQuery(
                        target.getLocationId(), coordinates.longitude(), coordinates.latitude()));
                if (rows == 1) updated++;
                else skipped++;
            } catch (SgisApiException | IllegalArgumentException e) {
                failures.add(new GeocodingBatchResult.Failure(target.getLocationId(), e.getMessage()));
            } catch (DataAccessException e) {
                failures.add(new GeocodingBatchResult.Failure(target.getLocationId(), "Coordinate update failed"));
            }
        }
        return new GeocodingBatchResult(targets.size(), updated, skipped, List.copyOf(failures));
    }

    public List<GeoCordCommonDto.GeoTarget> geoTargets() {
        return mapper.geoTargets();
    }

    public int updateGeoTargets(GeoCordCommonDto.CoordiQuery query) {
        return mapper.updateGeoTargets(query);
    }
}
