package org.example.myproject.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

@Configuration
public class SgisClientConfig {
    @Bean
    public RestClient sgisRestClient(
            @Value("${sgis.base-url:https://sgisapi.mods.go.kr}") String baseUrl) {
        var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5000);
        factory.setReadTimeout(10000);
        // Dedicated client avoids logging credentials carried in query parameters.
        return RestClient.builder().baseUrl(baseUrl).requestFactory(factory)
                .defaultHeader("Accept", "application/json").build();
    }
}

