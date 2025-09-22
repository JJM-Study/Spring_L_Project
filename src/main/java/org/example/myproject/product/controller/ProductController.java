package org.example.myproject.product.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.controller.AuthController;
import org.example.myproject.product.dto.ProductDetailDto;
import org.example.myproject.product.dto.ProductDto;
import org.example.myproject.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    private static final Logger logger = LogManager.getLogger(ProductController.class);


    // https://gangnam-americano.tistory.com/18 // 페이지네이션 구현 필요. 참고할 것.

    /*필터 조건을 더 확장하게 된다면, ModelAttribute 및 Search용 필터를 추가로 만드는 걸 고려할 것.*/
    @GetMapping("/products")
    public String selectProductList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "30") int pCount, Model model) {
        List<ProductDto> productList = productService.selectProductList(page, pCount);

        logger.info("productList : " + productList.size());

        model.addAttribute("pageTitle", "상품");
        model.addAttribute("itemList", productList);

        return "product/product-list";
    }

    @GetMapping("/detail/{prodNo}")
    public String selectProductDetail(@PathVariable("prodNo") String prodNo, Model model) {
        ProductDetailDto imageList = productService.selectProductDetail(prodNo);
        model.addAttribute("layoutBody", "/WEB-INF/jsp/product/product-detail.jsp");
        model.addAttribute("pageTitle", imageList.getProdName());
        model.addAttribute("itemList", imageList);

//        return "product/product-detail";
        return "layout/main-layout";
    }
}
