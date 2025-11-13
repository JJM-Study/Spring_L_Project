package org.example.myproject.order.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.service.AuthService;
import org.example.myproject.auth.service.CustomUserDetailService;
import org.example.myproject.cart.dto.CartDto;
import org.example.myproject.cart.service.CartService;
import org.example.myproject.config.Pagination;
import org.example.myproject.error.BusinessException;
import org.example.myproject.error.ErrorCode;
import org.example.myproject.order.dto.*;
import org.example.myproject.order.service.OrderService;
import org.example.myproject.product.dto.ProductImageDto;
import org.example.myproject.product.service.ProductService;
import org.example.myproject.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {
// 전체 선택 및 주문, 주문 실패 등 구현

    private static final Logger logger = LogManager.getLogger(CustomUserDetailService.class);

    @Autowired
    OrderService orderService;

    @Autowired
    StockService stockService;

    @Autowired
    ProductService productService;

    @Autowired
    CartService cartService;

    @Autowired
    AuthService authService;

    // 바로 주문 받기용임.
    @PostMapping("/from-cart")
    public ResponseEntity<Map<String, Object>> orderFromCart(@RequestBody List<Long> cartNos, HttpServletRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {

            // 설계 원칙에 따라서 service에서 CartToOrder 구현
            Map<String, Object> result = cartService.convertCartNosToOrderItems(cartNos);
            OrderDto order = (OrderDto) result.get("orderMaster");
            List<CartDto> cartDto = (List<CartDto>) result.get("cartNos");

            logger.info("order (OrderController): " + order);
            List<OrderDetailDto> orderDetails = (List<OrderDetailDto>) result.get("orderDetails");

            // 2025/11/10 추가 HttpServletRequest 방식에서 컨트롤러에서 직접 Id 전달하는 방식으로 수정함.  // 동시성 테스트 불편.
            String userId = authService.getAuthenticUserId(request);

            // service 추가
            //orderService.createOrder(order, orderDetails, cartDto, request);
            orderService.createOrder(order, orderDetails, cartDto, userId);

            response.put("success", true);
            response.put("message", "주문 성공 (결제 생략)");
            response.put("orderId", order.getOrderNo());

            return ResponseEntity.ok(response);

            // 2025/10/08 Business 예외 추가.
        } catch (BusinessException e) {
            response.put("message", e.getMessage());
            response.put("success", false);
            return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            logger.info("주문 실패" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/order_prod")
    public ResponseEntity<Map<String, Object>> orderNow(@RequestBody OrderRequestDto request, HttpServletRequest httpServletRequest) throws Exception {
        Map<String, Object> response = new HashMap<>();
        // 주문 시, 제품 번호를 읽는 것 외에도 다른 조치를 통해 멱등성 등 보장하는 방법에 대해서 고민할 것.


        Long prodNo = request.getProdNo();
        Integer qty = request.getQty();
        Integer price = productService.selectNowOrdProduct(prodNo).getPrice();

        // 2025/10/20 추가 . 이중 인증
        Integer totalPrice = request.getTotalPrice();

        if (totalPrice != price * qty) {
            throw new IllegalArgumentException("가격 불일치");// 비즈니스 예외로 수정 및 UI에 응답 반영할 것.
        }

//        if (price == null) {
//            // 이번 기회에 여기에다가 전역 에러 정의해서 처리해보자.
//            throw new ProductNotFoundException("주문 상품이 존재하지 않습니다.");
//        }

        OrderDto order = new OrderDto();
        // UserDetail DTO
        order.setTotalAmount(qty);
        order.setTotalPrice((long) totalPrice);
        OrderDetailDto orderDetail = new OrderDetailDto();
        orderDetail.setProdNo(prodNo);
        orderDetail.setQty(qty);
        orderDetail.setPrice(price);

        // 2025/11/10 추가
        String usreId = authService.getAuthenticUserId(httpServletRequest);
        // 지금 보니 createOrder 메소드의 order 파라미터는 불필요하다. 장바구니가 안정화 되면, 제거하는 방향으로 리펙토링.
        // 지금은 order.setAmount(qty); 정도만 넣어서 일단 넘기는 식으로 하자.
        //List.of로 감싸는 걸로...
        //String orderId = orderService.createOrder(order, List.of(orderDetail), Collections.emptyList(), httpServletRequest);
        String orderId = orderService.createOrder(order, List.of(orderDetail), Collections.emptyList(), usreId);


        logger.info("orderId" + orderId);
        //  success, message 등 키-값을 모듈 형태로 관리하는 방법은 없는 지 나중에 고민.
        response.put("success", true);
        response.put("message", "주문 성공 (결제 생략)");
        response.put("orderId", orderId);


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/result")
    public String ordResult(@RequestParam("orderId") String orderId, Model model, HttpServletRequest request) {
        model.addAttribute("orderId", orderId);
        logger.info("check orderId=" + orderId);

//        orderService.selectOrderNum()
//
//        model.addAttribute()

        String userId = authService.getAuthenticUserId(request);

        model.addAttribute("layoutBody", "/WEB-INF/jsp/order/order-conform.jsp");

//        orderService.
        // 여기에 주문 세부 정보를 조회하는 서비스 추가.
         OrderInfoDto orderInfo = orderService.orderDetails(orderId, userId);

         String defaultImage = "/assets/images/No_Image.png";

         for (OrderInfoProductDto item : orderInfo.getOrdInfoProdList()) {

             String mainPath = item.getImageList().stream()
                     .filter(img -> Boolean.TRUE.equals(img.getIsMain()))
                     .findFirst()
                     .map(ProductImageDto::getImageUrl)
                     .orElse(defaultImage);

             item.setMainImgPath(mainPath);
         }


         model.addAttribute("orderInfo", orderInfo);
         logger.info("orderInfo :" + orderInfo);

        return "layout/main-layout";
    }

    @GetMapping("/ordlist")
    public String orderList(@RequestParam(defaultValue = "0") int cPage, @RequestParam(defaultValue = "20") int pageSize, Model model, HttpServletRequest request) {
        List<OrderListDTO> orderList = new ArrayList<>();


        String userId = authService.getAuthenticUserId(request);

        //int listCnt = orderService.selectOrdListCount();
        int listCnt = orderService.selectOrdListCount(userId);

        Pagination pagination = new Pagination(cPage, pageSize, listCnt);

        //orderList = orderService.orderList(pageSize, pagination.getOffset());
        orderList = orderService.orderList(pageSize, pagination.getOffset(), userId);


        model.addAttribute("pageTitle", "상품 로그");
        model.addAttribute("pagination", pagination);
        model.addAttribute("orderList", orderList);
        model.addAttribute("pageUrl", "/order/ordlist");
        model.addAttribute("layoutBody", "/WEB-INF/jsp/order/order-list.jsp");


        return "layout/main-layout";
    }

}
