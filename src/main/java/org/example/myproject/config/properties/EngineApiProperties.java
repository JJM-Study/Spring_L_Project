package org.example.myproject.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@ConfigurationProperties("external-api.engine-api")
@Getter
@Setter
@Configuration
public class EngineApiProperties {
    private String baseUrl;
    private String spatialPath;
}
