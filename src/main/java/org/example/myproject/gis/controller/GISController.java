package org.example.myproject.gis.controller;

import jdk.jfr.StackTrace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.tags.shaded.org.apache.xalan.lib.NodeInfo;
import org.example.myproject.config.properties.EngineApiProperties;
import org.example.myproject.error.ErrorCode;
import org.example.myproject.error.ExternalServiceExceptionHandler;
import org.example.myproject.gis.dto.InputGISDto;
import org.example.myproject.gis.dto.OutputGISDto;

import org.example.myproject.shop.dto.ShopDTO;
import org.example.myproject.shop.server.ShopService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

// 2026/05/06 추가
@Controller
public class GISController {
    private static final Logger logger = LogManager.getLogger(GISController.class);

    private final WebClient webClient;

    private final ShopService shopService;

    private final EngineApiProperties properties;

    public GISController(WebClient.Builder webClientBuilder, EngineApiProperties properties, ShopService shopService) {
        logger.info("baseUrl :" + properties.getBaseUrl());
        this.webClient = webClientBuilder
                .baseUrl(properties.getBaseUrl())
                .build();
        this.properties = properties;
        this.shopService = shopService;
    }

    @ResponseBody
    @PostMapping("/nearby")
    public Mono<OutputGISDto> UseGIS (@RequestBody InputGISDto request) {

        logger.info("input_log : " + request);
        logger.info("BaseUrl : " + properties.getBaseUrl() + "\nSpatialPath : " + properties.getSpatialPath());

        return webClient
                .post()
                //.uri(properties.getBaseUrl() + properties.getSpatialPath() + "/nearby")
                .uri(properties.getBaseUrl() + properties.getSpatialPath() + "/nearby") // 의도적 에러 내기.
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OutputGISDto.class)
//                .doOnError(WebClientResponseException.class, e -> {
//                    logger.info("Http Status Code {}", e.getStatusCode());
//                    logger.info("Response Body {}", e.getResponseBodyAsString());
//                });
                .onErrorMap(e -> new ExternalServiceExceptionHandler(ErrorCode.ENGINE_SERVICE_UNAVAILABLE, e)) // Http 에러도 출력 가능하도록 해버리자.
                .doOnSuccess(e -> logger.info("result to external API {}", e))
                .flatMap(e -> {

                    List<String> externalIds = e.getNearbyNodes().stream()
                            .flatMap(node -> node.getExternalId().stream())
                            .toList();


                    return shopService.selectShopId(externalIds)
                            .map(shopDTO -> {
                                List<OutputGISDto.NodeInfo.ShopInfo> shopInfo = shopDTO.stream().map(shop -> {
                                            OutputGISDto.NodeInfo.ShopInfo info = new OutputGISDto.NodeInfo.ShopInfo();

                                            info.setShopId(shop.getShopId());
                                            info.setShopName(shop.getShopName());
                                            info.setCategoryLarge(shop.getCategoryLarge());
                                            info.setCategoryMid(shop.getCategoryMid());
                                            info.setRoadAddress(shop.getRoadAddress());

                                            return info;
                                        })
                                        .toList();
                                // JSON으로 직렬화 단계에서 매칭을 시키자.
                                logger.info("e : {}", e);

                                try {

                                    e.getNearbyNodes().forEach(node -> {

                                        List<OutputGISDto.NodeInfo.ShopInfo> matchedShopInfo =
                                                shopInfo.stream()
                                                        .filter(shop ->
                                                                node.getExternalId().contains(shop.getShopId())
                                                        ).toList();

                                        node.setShopInfo(matchedShopInfo);

                                    });



                                } catch(Exception a) {
                                    a.printStackTrace();
                                }

                                return e;
                            });
                });

        }
    }


