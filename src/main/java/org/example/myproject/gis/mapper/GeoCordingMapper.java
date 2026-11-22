package org.example.myproject.gis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.myproject.gis.dto.GeoCordCommonDto;


import java.util.List;

@Mapper
public interface GeoCordingMapper {

    List<GeoCordCommonDto.GeoTarget> geoTargets();

    int updateGeoTargets(GeoCordCommonDto.CoordiQuery query);
}
