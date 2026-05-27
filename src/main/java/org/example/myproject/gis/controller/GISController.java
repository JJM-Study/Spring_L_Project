package org.example.myproject.gis.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.config.properties.EngineApiProperties;
import org.example.myproject.error.BusinessException;
import org.example.myproject.error.ErrorCode;
import org.example.myproject.error.ExternalServiceExceptionHandler;
import org.example.myproject.error.GlobalExceptionHandler;
import org.example.myproject.gis.dto.InputGISDto;
import org.example.myproject.gis.dto.OutputGISDto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

// 2026/05/06 추가
@Controller
public class GISController {
    private static final Logger logger = LogManager.getLogger(GISController.class);

    private final WebClient webClient;

    private EngineApiProperties properties;

    public GISController(WebClient.Builder webClientBuilder, EngineApiProperties properties) {
        logger.info("baseUrl :" + properties.getBaseUrl());
        this.webClient = webClientBuilder
                .baseUrl(properties.getBaseUrl())
                .build();
        this.properties = properties;
    }

    @ResponseBody
    @PostMapping("/nearby")
    public Mono<OutputGISDto> UseGIS (@RequestBody InputGISDto request){

        logger.info("input_log : " + request);
        logger.info("BaseUrl : " + properties.getBaseUrl() + "\nSpatialPath : " + properties.getSpatialPath());
        return webClient
                .post()
                //.uri(properties.getBaseUrl() + properties.getSpatialPath() + "/nearby")
                .uri(properties.getBaseUrl() + properties.getSpatialPath() + "/nearby1") // 의도적 에러 내기.
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OutputGISDto.class)
//                .doOnError(WebClientResponseException.class, e -> {
//                    logger.info("Http Status Code {}", e.getStatusCode());
//                    logger.info("Response Body {}", e.getResponseBodyAsString());
//                });
                .onErrorMap(e -> new ExternalServiceExceptionHandler(ErrorCode.ENGINE_SERVICE_UNAVAILABLE, e)); // Http 에러도 출력 가능하도록 해버리자.
    }


}
