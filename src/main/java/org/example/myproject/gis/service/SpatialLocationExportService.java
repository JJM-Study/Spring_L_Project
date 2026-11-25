package org.example.myproject.gis.service;

import org.example.myproject.gis.dto.OutputGISDto;
import org.example.myproject.gis.dto.OutputGISDto.NodeInfo.ShopInfo;
import org.example.myproject.gis.dto.SpatialLocationExportDto;
import org.example.myproject.gis.mapper.SpatialLocationExportMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class SpatialLocationExportService {
    private final SpatialLocationExportMapper mapper;

    public SpatialLocationExportService(SpatialLocationExportMapper mapper) {
        this.mapper = mapper;
    }

    public List<SpatialLocationExportDto> findValidLocations() {
        return mapper.findValidLocations();
    }

    public void attachSellingLocations(OutputGISDto result) {
        var nodes = result.getNearbyNodes();
        var ids = nodes.stream().flatMap(node -> node.getExternalId().stream()).distinct().toList();
        Map<String, ShopInfo> byId = ids.isEmpty() ? Map.of() : mapper.findSellingLocations(ids).stream()
                .collect(Collectors.toMap(ShopInfo::getLocationId, Function.identity()));
        for (var node : nodes) {
            node.setShopInfo(node.getExternalId().stream().map(byId::get).filter(Objects::nonNull).toList());
        }
    }
}
