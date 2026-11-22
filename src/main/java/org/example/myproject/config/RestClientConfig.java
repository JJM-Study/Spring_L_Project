package org.example.myproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    // 기회되면, 프레임워크 큰 틀의 상속 및 데이터 전달 관계를 도표화해서 분석을 더 해보는 게 좋을 것 같다.
    @Bean
    public RestClient restClient(RestClient.Builder builder) {
        return builder
                .defaultHeader("Accept", "application/json")
                .build();


    }
}
