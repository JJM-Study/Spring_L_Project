package org.example.myproject.order.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.service.CustomUserDetailService;
import org.example.myproject.cart.dto.CartDto;
import org.example.myproject.cart.service.CartService;
import org.example.myproject.error.BusinessException;
import org.example.myproject.error.ErrorCode;
import org.example.myproject.order.dto.OrderDetailDto;
import org.example.myproject.order.dto.OrderDto;
import org.example.myproject.order.dto.OrderListDTO;
import org.example.myproject.order.dto.OrderRequestDto;
import org.example.myproject.order.service.OrderService;
import org.example.myproject.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    ProductService productService;

    @Autowired
    CartService cartService;

    // 바로 주문 받기용임.
    @PostMapping("/from-cart")
    public ResponseEntity<Map<String, Object>> orderFromCart(@RequestBody List<Long> cartNos) {
        Map<String, Object> response = new HashMap<>();
        try {

            // 설계 원칙에 따라서 service에서 CartToOrder 구현
            Map<String, Object> result = cartService.convertCartNosToOrderItems(cartNos);
            OrderDto order = (OrderDto) result.get("orderMaster");
            List<CartDto> cartDto = (List<CartDto>) result.get("cartNos");

            logger.info("order (OrderController): " + order);
            List<OrderDetailDto> orderDetails = (List<OrderDetailDto>) result.get("orderDetails");

            // service 추가
            orderService.createOrder(order, orderDetails, cartDto);

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
            logger.info("주문 실패" + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @PostMapping("/order_prod")
    public ResponseEntity<Map<String, Object>> orderNow(@RequestBody OrderRequestDto request) {
        Map<String, Object> response = new HashMap<>();
        // 주문 시, 제품 번호를 읽는 것 외에도 다른 조치를 통해 멱등성 등 보장하는 방법에 대해서 고민할 것.


        Long prodNo = request.getProdNo();
        Integer qty = request.getQty();
        Integer price = productService.selectNowOrdProduct(prodNo).getPrice();

//        if (price == null) {
//            // 이번 기회에 여기에다가 전역 에러 정의해서 처리해보자.
//            throw new ProductNotFoundException("주문 상품이 존재하지 않습니다.");
//        }

        OrderDto order = new OrderDto();
        // UserDetail DTO
        order.setTotalAmount(qty);

        OrderDetailDto orderDetail = new OrderDetailDto();
        orderDetail.setProdNo(prodNo);
        orderDetail.setQty(qty);
        orderDetail.setPrice(price);



        // 지금 보니 createOrder 메소드의 order 파라미터는 불필요하다. 장바구니가 안정화 되면, 제거하는 방향으로 리펙토링.
        // 지금은 order.setAmount(qty); 정도만 넣어서 일단 넘기는 식으로 하자.
        //List.of로 감싸는 걸로...
        String orderId = orderService.createOrder(order, List.of(orderDetail), Collections.emptyList());

        logger.info("orderId" + orderId);
        //  success, message 등 키-값을 모듈 형태로 관리하는 방법은 없는 지 나중에 고민.
        response.put("success", true);
        response.put("message", "주문 성공 (결제 생략)");
        response.put("orderId", orderId);


        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @GetMapping("/result")
    public String ordResult(@RequestParam("orderId") String orderId, Model model) {
        model.addAttribute("orderId", orderId);
        logger.info("check orderId=" + orderId);
        model.addAttribute("layoutBody", "/WEB-INF/jsp/order/order-conform.jsp");

        return "layout/main-layout";
    }

    @GetMapping("/orderList")
    public String orderList(Model model) {
        List<OrderListDTO> orderList = new ArrayList<>();

        orderList = orderService.orderList();

        model.addAttribute("orderList", orderList);
        model.addAttribute("layoutBody", "/WEB-INF/jsp/order/order-list.jsp");

        return "layout/main-layout";
    }

}
