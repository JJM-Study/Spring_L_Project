package org.example.myproject.product.controller;

import org.example.myproject.product.dto.ProductDetailDto;
import org.example.myproject.product.dto.ProductDto;
import org.example.myproject.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @GetMapping("/products")
    public String selectProductList(Model model) {
        List<ProductDto> productList = productService.selectProductList();
        model.addAttribute("pageTitle", "상품");
        model.addAttribute("itemList", productList);

        return "product/product-list";
    }

    @GetMapping("/productDetail")
    public String selectProductDetail(@RequestParam("prodNo") String prodNo, Model model) {
        ProductDetailDto imageList = productService.selectProductDetail(prodNo);
        model.addAttribute("pageTitle", imageList.getProdName());
        model.addAttribute("itemList", imageList);

        return "/product/productDetail";
    }
}
