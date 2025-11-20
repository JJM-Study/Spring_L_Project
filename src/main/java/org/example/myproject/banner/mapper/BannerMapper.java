package org.example.myproject.banner.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.myproject.banner.dto.BannerDto;

import java.util.List;

@Mapper
public interface BannerMapper {
    List<BannerDto> selectHomeBanners();
}
