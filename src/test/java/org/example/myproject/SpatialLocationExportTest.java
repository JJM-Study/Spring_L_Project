package org.example.myproject;

import org.apache.ibatis.builder.xml.XMLMapperBuilder;
import org.apache.ibatis.datasource.unpooled.UnpooledDataSource;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.example.myproject.config.properties.EngineApiProperties;
import org.example.myproject.gis.client.SpatialEngineClient;
import org.example.myproject.gis.controller.GISController;
import org.example.myproject.gis.mapper.SpatialLocationExportMapper;
import org.example.myproject.gis.service.SpatialLocationExportService;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClient;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.*;
import static org.springframework.test.web.client.response.MockRestResponseCreators.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SpatialLocationExportTest {
    @Test
    void uploadsDatabaseSnapshotAndSearchesSeparatelyByLocationId() throws Exception {
        var db = new UnpooledDataSource("org.h2.Driver", "jdbc:h2:mem:spatial;MODE=MySQL", "sa", "");
        try (var connection = db.getConnection(); var statement = connection.createStatement()) {
            statement.execute("CREATE TABLE tb_selling_location (location_id BIGINT PRIMARY KEY, longitude DOUBLE, latitude DOUBLE, ddt DATETIME, location_name VARCHAR(100), address VARCHAR(255))");
            statement.execute("""
                    INSERT INTO tb_selling_location VALUES
                    (101,127,37,NULL,'First','First address'), (104,0,0,NULL,'Second','Second address'),
                    (102,NULL,37,NULL,'Missing',''), (103,181,37,NULL,'Invalid',''),
                    (105,127,37,CURRENT_TIMESTAMP,'Deleted','')
                    """);
            var config = new Configuration(new Environment("test", new JdbcTransactionFactory(), db));
            String xml = "src/main/resources/mybatis/mappers/gis/spatialLocationExportMapper.xml";
            try (var input = Files.newInputStream(Path.of(xml))) {
                new XMLMapperBuilder(input, config, xml, config.getSqlFragments()).parse();
            }
            try (var session = new SqlSessionFactoryBuilder().build(config).openSession()) {
                var builder = RestClient.builder();
                var server = MockRestServiceServer.bindTo(builder).build();
                var settings = new EngineApiProperties();
                settings.setBaseUrl("http://engine");
                settings.setSpatialPath("/api/v1/spatial");
                settings.setClientId(3L);
                settings.setApiKey("test-key");
                var locations = new SpatialLocationExportService(session.getMapper(SpatialLocationExportMapper.class));
                var mvc = MockMvcBuilders.standaloneSetup(new GISController(new SpatialEngineClient(builder, settings), locations)).build();
                String base = "http://engine/api/v1/spatial/clients/3";
                server.expect(requestTo(base + "/locations")).andExpect(method(HttpMethod.PUT))
                        .andExpect(header("X-API-Key", "test-key"))
                        .andExpect(content().json("""
                                {"locations":[{"externalId":"101","lon":127,"lat":37},{"externalId":"104","lon":0,"lat":0}]}
                                """, true))
                        .andRespond(withSuccess("""
                                {"clientId":3,"dataVersion":1,"locationCount":2,"status":"READY","loadedAt":null}
                                """, MediaType.APPLICATION_JSON));
                String query = "{\"lon\":0,\"lat\":0,\"k\":1}";
                server.expect(requestTo(base + "/nearby")).andExpect(method(HttpMethod.POST))
                        .andExpect(header("X-API-Key", "test-key")).andExpect(content().json(query, true))
                        .andRespond(withSuccess("""
                                {"clientId":3,"dataVersion":1,"center_lon":0,"center_lat":0,
                                 "nearby_nodes":[{"node_id":1,"external_id":["104"],"distance":0,"lon":0,"lat":0}]}
                                """, MediaType.APPLICATION_JSON));
                server.expect(requestTo(base + "/nearby")).andRespond(withStatus(HttpStatus.CONFLICT)
                        .contentType(MediaType.APPLICATION_JSON).body("{\"code\":\"INDEX_NOT_LOADED\"}"));
                mvc.perform(put("/api/spatial/locations")).andExpect(status().isOk())
                        .andExpect(jsonPath("$.locationCount").value(2));
                mvc.perform(post("/nearby").contentType(MediaType.APPLICATION_JSON).content(query))
                        .andExpect(status().isOk()).andExpect(jsonPath("$.dataVersion").value(1))
                        .andExpect(jsonPath("$.nearby_nodes[0].shop_info[0].location_id").value("104"))
                        .andExpect(jsonPath("$.nearby_nodes[0].shop_info[0].shop_name").value("Second"));
                mvc.perform(post("/nearby").contentType(MediaType.APPLICATION_JSON).content(query))
                        .andExpect(status().isConflict()).andExpect(jsonPath("$.code").value("INDEX_NOT_LOADED"));
                server.verify();
            }
        }
    }
}
