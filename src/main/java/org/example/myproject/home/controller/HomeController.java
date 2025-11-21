package org.example.myproject.home.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.service.AuthService;
import org.example.myproject.banner.dto.BannerDto;
import org.example.myproject.banner.service.BannerService;
import org.example.myproject.product.dto.ProductDisplayListDto;
import org.example.myproject.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    private static final Logger logger = LogManager.getLogger(HomeController.class);

    @Autowired
    ProductService productService;

    @Autowired
    AuthService authService;

    @Autowired
    BannerService bannerService;

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request) {

        String userId = authService.getAuthenticUserId(request);


        List<ProductDisplayListDto> bestProducts = productService.displayProductList(userId, "BEST");

        List<BannerDto> banners = bannerService.selectHomeBanners();
        logger.info("bestProducts : " + bestProducts);
        model.addAttribute("layoutBody", "/WEB-INF/jsp/home.jsp");

        // frontend에 이미지를 가져오는 일련의 과정들을 module화 할 순 없는 걸까?
        model.addAttribute("banners", banners);

        model.addAttribute("bestList", bestProducts);
        List<ProductDisplayListDto> latestProducts = productService.displayProductList(userId, "latest");

        model.addAttribute("latestList", latestProducts);
        logger.info("home test");
        return "layout/main-layout";
    }

}
