package org.example.myproject.banner.service;

import org.example.myproject.banner.dto.BannerDto;
import org.example.myproject.banner.mapper.BannerMapper;
import org.example.myproject.file.dto.FileDownloadDto;
import org.example.myproject.product.dto.ProductCheckFileDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BannerService {

    @Autowired
    BannerMapper bannerMapper;

    @Value("${file.resource-path}")
    private String resourcePath;


    public List<BannerDto> selectHomeBanners() {

//        return bannerMapper.selectHomeBanners();

        List<BannerDto> bannerDtos = bannerMapper.selectHomeBanners();

        String fullPath = resourcePath.replace("**","") + "banners";


        bannerDtos.forEach(dto -> dto.setDomainUrl(fullPath));

        return bannerDtos;

    }


//    public FileDownloadDto getDownloadInfo(Long prodNo) {
//
//        ProductCheckFileDto productCheckFileDto = productFileMapper.checkDownloadFile(prodNo, userId);
//
//        Long fileId = productCheckFileDto.getFileId();
//
//
//        return fileService.loadFile(fileId);
//    }

}
