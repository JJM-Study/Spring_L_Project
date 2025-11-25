package org.example.myproject.product.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.service.AuthService;
import org.example.myproject.cart.service.CartService;
import org.example.myproject.common.DateUtils;
import org.example.myproject.config.Pagination;
import org.example.myproject.product.dto.*;
import org.example.myproject.product.service.ProductService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import java.util.List;

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    AuthService authService;

    @Autowired
    CartService cartService;

    @Autowired
    ObjectMapper objectMapper;

    private static final Logger logger = LogManager.getLogger(ProductController.class);


    // https://gangnam-americano.tistory.com/18 // 페이지네이션 구현 필요. 참고할 것.

    /*필터 조건을 더 확장하게 된다면, ModelAttribute 및 Search용 필터를 추가로 만드는 걸 고려할 것.*/
    // pPage = 현재 페이지 , pCount = 페이지 갯수.
    @GetMapping("/products")
    //public String selectProductList(@RequestParam(defaultValue = "0") int cPage, @RequestParam(defaultValue = "10") int pageSize, Model model) {
    public String selectProductList(@RequestParam(defaultValue = "0") int cPage, @RequestParam(defaultValue = "10") int pageSize, @RequestParam(required = false) String title, Model model, HttpServletRequest request) {

//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        Object principle = authentication.getPrincipal();
//
//        String userId = null;
//
//        if (principle instanceof UserDetails) {
//            userId = ((UserDetails) principle).getUsername();
//        }

        String userId = authService.getAuthenticUserId(request);


//        int listCnt = productService.selectProductCount();
        int listCnt = productService.selectProductCount(title);
        logger.info("search-title :" + title);
        Pagination pagination = new Pagination(cPage, pageSize, listCnt);


        logger.info("listCnt : " + listCnt);
        logger.info("cPage :" + pageSize + " offset : " +  pagination.getOffset());
        //List<ProductDto> productList = productService.selectProductList(pageSize, pagination.getOffset(), title);
        List<ProductDto> productList = productService.selectProductList(pageSize, pagination.getOffset(), title, userId);

        logger.info("productList : " + productList.size());
        logger.info("EndPage : " + pagination.getEndPage() + "StartPage : " + pagination.getStartPage());
        logger.info("lists : " + productList);

        model.addAttribute("pageTitle", "상품");
        model.addAttribute("itemList", productList);
        model.addAttribute("pagination", pagination);
        model.addAttribute("pageUrl", "/product/products");
        model.addAttribute("layoutBody", "/WEB-INF/jsp/product/product-list.jsp");
        model.addAttribute("title", title);



        logger.info("prev :" + pagination.getPrevPage());
        logger.info("next :" + pagination.getNextPage());
        logger.info("title : " + title);

        return "layout/main-layout";
//        return "product/product-list";
    }

    @GetMapping("/detail/{prodNo}")
    //public String selectProductDetail(@PathVariable("prodNo") String prodNo, Model model) {
    public String selectProductDetail(@PathVariable("prodNo") String prodNo, Model model, HttpServletRequest request) {

        try {

            String userId = authService.getAuthenticUserId(request);

            Safelist safelist = Safelist.basicWithImages();

            ProductDetailDto itemDetail = productService.selectProductDetail(prodNo, userId);
            if(itemDetail.getDetailDesc() != null) {
                itemDetail.setDetailDesc(Jsoup.clean(itemDetail.getDetailDesc(), safelist));
            }
            logger.info("itemDetail" + itemDetail);
//        logger.info("imageList :" + imageList.get);

//        String itemDetailJson = gson.toJson(itemDetail);

                String itemDetailJsonOrgin = objectMapper.writeValueAsString(itemDetail);

                String itemDetailJson = Jsoup.clean(itemDetailJsonOrgin, safelist);

                logger.info("itemDetailJson :" + itemDetailJson);

            itemDetail.setSalesDtFormatted(DateUtils.formatLocalDateTime(itemDetail.getSalesDt()));

            SendImageDto sendImageDTO = productService.isMainImage(itemDetail);

            logger.info("itemDetail : " + itemDetail);

//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//            // 익명 유저는 쿠키 식별을 통해서 인증하는 관련 메소드를 추가할 필요 있음.
//            String userId = null;
//
//            Object principal = authentication.getPrincipal();
//
//            if(principal instanceof UserDetails) {
//                userId = ((UserDetails) principal).getUsername();
//            } else {
//                userId = (String) request.getAttribute("anonymous_user_id");
//                if (userId == null) {
//                    throw new BusinessException(ErrorCode.ANONYMOUS_NOT_FOUND);
//                }
//            }


            boolean isInCart = cartService.isInCart(prodNo, userId);

            model.addAttribute("isInCart", isInCart);

            model.addAttribute("layoutBody", "/WEB-INF/jsp/product/product-detail.jsp");
            model.addAttribute("pageTitle", itemDetail.getProdName());
            model.addAttribute("itemList", itemDetail);
            model.addAttribute("itemDetailJson", itemDetailJson);
//        model.addAttribute("itemListJson", itemDetailJson);
            model.addAttribute("subImages", sendImageDTO.getSubImages());
            model.addAttribute("mainImages", sendImageDTO.getMainImage());

            logger.info("isInCart :" + isInCart);
//        return "product/product-detail";

        }
        catch(Exception e) {
            logger.error("Exception : " + ProductController.class.getSimpleName() + "예외 발생" + e.getMessage());
        }

        return "layout/main-layout";
    }

//    @GetMapping("/best")
//    public List<ProductBestsellerDto> displayProductList(String userId) {
//        return productService.displayBestProducts(userId, "best");
//    }

}
