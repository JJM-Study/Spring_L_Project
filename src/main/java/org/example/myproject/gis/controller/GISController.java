package org.example.myproject.gis.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
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
    }

    @ResponseBody
    public Mono<OutputGISDto> UseGIS (@RequestBody InputGISDto request){

        logger.info("input_log : " + request);

        return webClient
                .post()
                .uri("/nearby")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OutputGISDto.class)
                .onErrorMap(e -> new ExternalServiceExceptionHandler(ErrorCode.ENGINE_SERVICE_UNAVAILABLE));

    }
}
