package org.example.myproject.gis.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.gis.service.GeoCodingService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class GeoCodingController {
    // https://sgis.mods.go.kr/developer/html/newOpenApi/api/dataApi/addressBoundary.html?utm_source=chatgpt.com
    // -> 통계지리정보 SGIS 먼저 사용하고, 이후 복잡도 확장 시 행정안전부 주소 API 검토.


    private static final Logger logger = LogManager.getLogger(GeoCodingController.class);

    private final GeoCodingService geoCodingService;


    public GeoCodingController(GeoCodingService geoCodingService) {
        this.geoCodingService = geoCodingService;
    }

    @GetMapping("/geocoding/kakao")
    public void GeoCoding() {

        // 해당 주소 실행 시 tb_selling_location 루프하며
        // address 카카오 전송 -> latitude, longtiude 받아와서 tb_selling_location에 update하는 로직.

        geoCodingService.GeoCoding();


    }
}
