package org.example.myproject.gis.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.myproject.gis.dto.SpatialLocationExportDto;
import org.example.myproject.gis.dto.OutputGISDto.NodeInfo.ShopInfo;
import java.util.List;

@Mapper
public interface SpatialLocationExportMapper {
    List<SpatialLocationExportDto> findValidLocations();
    List<ShopInfo> findSellingLocations(@Param("ids") List<String> ids);
}

