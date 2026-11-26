package org.example.myproject.gis.controller;

import org.example.myproject.common.controller.AbstractBaseController;
import org.example.myproject.gis.client.SpatialEngineClient;
import org.example.myproject.gis.dto.InputGISDto;
import org.example.myproject.gis.dto.OutputGISDto;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/views/")
public class GISViewController extends AbstractBaseController {

    SpatialEngineClient spatialEngineClient;

    GISViewController(SpatialEngineClient spatialEngineClient) {
        this.spatialEngineClient = spatialEngineClient;
    }

    @GetMapping("/gis")
    public String GISView(Model model, InputGISDto input) {


        // 테스트용. 이후엔 지우고 빈지도만 나오게 하거나, 아니면 현재 위치를 찾아서 알아서 조정시키는 로직 구현.
//        input.setK(3);
//        input.setLat(0.1);
//        input.setLon(0.3);
////
//        spatialEngineClient.nearby(input);

        return layout("/gis/gis.jsp", model);
    }

}
