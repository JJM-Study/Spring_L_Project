package org.example.myproject.EngineAPI;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.config.properties.EngineApiProperties;
import org.example.myproject.gis.dto.InputGISDto;
import org.example.myproject.gis.dto.OutputGISDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserter;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // 1. 서버 환경 강제 확정
@AutoConfigureWebTestClient
public class TestFastAPI {
    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private EngineApiProperties apiProperties;


    private static final Logger logger = LogManager.getLogger(TestFastAPI.class);

    @Test
    //public Mono<OutputGISDto> TestGIS (InputGISDto request) {
    void TestGIS() {

        InputGISDto request = new InputGISDto();
        // 서울역 기준
        request.setLat(37.5559);
        request.setLon(126.9723);
        request.setK(3);


        webTestClient
                .post()
                .uri(apiProperties.getBaseUrl() + apiProperties.getSpatialPath() + "/nearby")
                .bodyValue(request)
                .exchange()
                .expectStatus().isOk()
                .expectBody(OutputGISDto.class)
                .consumeWith(response -> {
                    logger.info("응답 바디: {}", response.getResponseBody());
                } // 응답과 request 다 받도록 수정하자.

                );
        }
    }