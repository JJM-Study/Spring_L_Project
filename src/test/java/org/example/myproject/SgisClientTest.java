package org.example.myproject;

import org.example.myproject.gis.client.sgis.*;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;

class SgisClientTest {
    @Test
    void cachesTokenAndEncodesAddressAndReadsCoordinates() {
        var builder = RestClient.builder().baseUrl("https://sgisapi.mods.go.kr");
        var server = MockRestServiceServer.bindTo(builder).build();
        var http = builder.build();
        var tokens = new SgisTokenProvider(http, "key", "secret");
        var client = new SgisGeocodingClient(http, tokens);
        server.expect(requestTo(org.hamcrest.Matchers.containsString("consumer_key=key")))
                .andRespond(withSuccess(tokenJson(System.currentTimeMillis() + 3600000), MediaType.APPLICATION_JSON));
        server.expect(request -> {
            String uri = request.getURI().toASCIIString();
            assertTrue(uri.contains("address=%EC%84%9C%EC%9A%B8%20%2B%26"));
            assertTrue(uri.contains("accessToken=test-token"));
        }).andRespond(withSuccess("""
                {"errCd":0,"result":{"matching":"0","resultdata":[{"x":"127.38","y":"36.36"}]}}
                """, MediaType.APPLICATION_JSON));
        var coordinates = client.geocode("서울 +&").orElseThrow();
        assertEquals(127.38, coordinates.longitude());
        assertEquals(36.36, coordinates.latitude());
        assertEquals("test-token", tokens.getAccessToken());
        server.verify();
    }

    @Test
    void rejectsExpiredAuthenticationResponse() {
        var builder = RestClient.builder().baseUrl("https://sgisapi.mods.go.kr");
        var server = MockRestServiceServer.bindTo(builder).build();
        var tokens = new SgisTokenProvider(builder.build(), "key", "secret");
        server.expect(anything()).andRespond(withSuccess(tokenJson(1), MediaType.APPLICATION_JSON));
        assertThrows(SgisApiException.class, tokens::getAccessToken);
        server.verify();
    }

    @Test
    void returnsEmptyForNoMatchAndRejectsApiErrors() {
        var builder = RestClient.builder().baseUrl("https://sgisapi.mods.go.kr");
        var server = MockRestServiceServer.bindTo(builder).build();
        var http = builder.build();
        var client = new SgisGeocodingClient(http, new SgisTokenProvider(http, "key", "secret"));
        server.expect(anything()).andRespond(withSuccess(tokenJson(System.currentTimeMillis() + 3600000), MediaType.APPLICATION_JSON));
        server.expect(anything()).andRespond(withSuccess("{\"errCd\":-100}", MediaType.APPLICATION_JSON));
        server.expect(anything()).andRespond(withSuccess("{\"errCd\":-999}", MediaType.APPLICATION_JSON));
        assertTrue(client.geocode("없는 주소").isEmpty());
        assertThrows(SgisApiException.class, () -> client.geocode("서울"));
        server.verify();
    }

    private String tokenJson(long expiry) {
        return "{\"errCd\":0,\"result\":{\"accessToken\":\"test-token\",\"accessTimeout\":\"" + expiry + "\"}}";
    }
}
