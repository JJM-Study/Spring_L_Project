package org.example.myproject.home.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.service.AuthService;
import org.example.myproject.product.dto.ProductBestsellerDto;
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

    @GetMapping("/home")
    public String home(Model model, HttpServletRequest request) {

        String userId = authService.getAuthenticUserId(request);

        List<ProductBestsellerDto> bestProducts = productService.displayBestProducts(userId);
        logger.info("bestProducts : " + bestProducts);
        model.addAttribute("layoutBody", "/WEB-INF/jsp/home.jsp");
        model.addAttribute("bestList", bestProducts);
        logger.info("home test");
        return "layout/main-layout";
    }

}
