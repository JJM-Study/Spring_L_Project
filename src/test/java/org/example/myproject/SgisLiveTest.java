package org.example.myproject;

import org.example.myproject.config.SgisClientConfig;
import org.example.myproject.gis.client.sgis.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import static org.junit.jupiter.api.Assertions.*;

/** Explicit opt-in: makes API requests but never connects to the application DB. */
@EnabledIfSystemProperty(named = "sgis.live", matches = "true")
class SgisLiveTest {
    @Test
    void authenticatesAndGeocodesPublicAddress() throws Exception {
        var properties = new Properties();
        try (Reader reader = Files.newBufferedReader(Path.of("src/main/resources/test.properties"), StandardCharsets.UTF_8)) {
            properties.load(reader);
        }
        var http = new SgisClientConfig().sgisRestClient("https://sgisapi.mods.go.kr");
        var tokens = new SgisTokenProvider(http, properties.getProperty("sgis.service-id"),
                properties.getProperty("sgis.security-key"));
        var coordinates = new SgisGeocodingClient(http, tokens)
                .geocode("대전광역시 서구 청사로 189").orElseThrow();
        assertTrue(coordinates.longitude() > 124 && coordinates.longitude() < 132);
        assertTrue(coordinates.latitude() > 33 && coordinates.latitude() < 39);
    }
}
