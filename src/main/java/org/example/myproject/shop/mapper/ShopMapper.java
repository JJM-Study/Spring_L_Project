package org.example.myproject.shop.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.myproject.gis.dto.OutputGISDto;
import org.example.myproject.shop.dto.ShopDTO;
import reactor.core.publisher.Mono;

import java.util.List;


@Mapper
public interface ShopMapper {

    List<ShopDTO> selectShopId(@Param("externalIds") List<String> externalIds);
}
