package org.example.myproject.banner.service;

import org.example.myproject.banner.dto.BannerDto;
import org.example.myproject.banner.mapper.BannerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerService {

    @Autowired
    BannerMapper bannerMapper;

    public List<BannerDto> selectHomeBanners() {

        return bannerMapper.selectHomeBanners();
    }


}
