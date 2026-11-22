package org.example.myproject.gis.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.gis.dto.GeoCordCommonDto;
import org.example.myproject.gis.dto.GeoSGISResponseDto;

import org.example.myproject.gis.mapper.GeoCordingMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
public class GeoCodingService {


    // https://dapi.kakao.com/v2/local/search/address.${FORMAT}

    private final GeoCordingMapper geoCordingMapper;

    @Value("${test.service-id}")
    String consumerKey;

    @Value("${test.security-key}")
    String securityKey;

    private final static Logger logger = LogManager.getLogger(GeoCodingService.class);

    RestClient restClient;

    public GeoCodingService(GeoCordingMapper geoCordingMapper,
                            RestClient.Builder builder,
                            String consumerKey,
                            String securityKey) {
        this.geoCordingMapper = geoCordingMapper;
        this.restClient = builder.baseUrl("https://sgisapi.mods.go.kr").build();
        this.consumerKey = consumerKey;
        this.securityKey = securityKey;
    }



    public void GeoCoding() {

        List<GeoCordCommonDto.GeoTarget> geoTargets = geoCordingMapper.geoTargets();
//
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK" + apiKey);


//        HttpEntity<Void> request = new HttpEntity<>(headers);

        Long locationId;

        logger.info("geoCoding list : " + geoTargets.size());


        double lon;
        double lat;

        logger.info("serviceId : " + serviceId);
        logger.info("securityKey : " + securityKey);




        try {

            // SGIS는 토큰 만료에 대한 callback이 구체적이지 않은 듯?



            if ()


            for (GeoCordCommonDto.GeoTarget targets : geoTargets) {

                String address = targets.getAddress();

                // 여기에서 4시간마다 초기화 되는 엑세스키 발급 받고



                // 그 다음 api로 보내는 걸 SGIS 로직에 맞춰서 수정할 것.
                String uri = UriComponentsBuilder.fromUriString("https://sgisapi.mods.go.kr/OpenAPI3/addr/geocodewgs84.json")
                        .queryParam("accessToken", token)
                        .queryParam("address", address)
                        .build();


                // https://sgis.mods.go.kr/developer/html/newOpenApi/api/develop/dvp.html  (WGS84)




                GeoSGISResponseDto
                        response = restClient.get()
                                             .uri(uri)
                                             .header("Authorization", "KakaoAK " + apiKey)
                                             .retrieve()
                                             .body(GeoSGISResponseDto.class);

                lon = response.getResultdata().getX();
                lat = response.getResultdata().getY();

                locationId = targets.getLocationId();


                int result = geoCordingMapper.updateGeoTargets(new GeoCordCommonDto.CoordiQuery(locationId, lon, lat));

                logger.info("geoCoding results : " + result);

            }

        } catch(Exception e) {
            logger.error("GeoCoding Error catch", e);
        }


    }

    public List<GeoCordCommonDto.GeoTarget> geoTargets() {
        return geoCordingMapper.geoTargets();
    }

    public int updateGeoTargets(GeoCordCommonDto.CoordiQuery query) {
        return geoCordingMapper.updateGeoTargets(query);
    }

}
