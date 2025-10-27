package org.example.myproject.cart.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.controller.AuthController;
import org.example.myproject.cart.dto.ChkCartItemDto;
import org.example.myproject.cart.mapper.CartMapper;
import org.example.myproject.cart.dto.CartDto;
import org.example.myproject.order.dto.OrderDetailDto;
import org.example.myproject.order.dto.OrderDto;
import org.example.myproject.product.dto.ProductPriceDto;
import org.example.myproject.product.service.ProductService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartService {

    private static final Logger logger = LogManager.getLogger(CartService.class);

    @Autowired
    private final CartMapper cartMapper;

    @Autowired
    private ProductService productService;

    public CartService(CartMapper cartMapper) {
        this.cartMapper = cartMapper;
    }

    public List<CartDto> selectOrderCartList(String orderColumn, String orderType, String userId) {
        List<String> allowedColumns = Arrays.asList("cart_no", "prod_name", "qty"); // 악의적 쿼리 삽입 방지 . 화이트리스트
        List<String> allowedType = Arrays.asList("asc", "desc");

        if (!allowedColumns.contains(orderColumn)) {
            orderColumn = "cart_no";
        }
        if (!allowedType.contains(orderType)) {
            orderType = "asc";
        }

        return cartMapper.selectOrderCartList(orderColumn, orderType, userId);
    }

    public int addToCart(CartDto cartDto) {
        return cartMapper.addToOrderCart(cartDto);
    }

    // Cart 테이블에서 가져오던 UserId를, 주문 시 Authentication 으로 가져오도록 수정하는 게 보완상 맞는 것 같다.
    public Map<String, Object> convertCartNosToOrderItems(@Param("cartNos") List<Long> cartNos) {
        Map<String, Object> result = new HashMap<>();
        try {
            logger.info("cartNos: " + cartNos);
            List<CartDto> cartItems = cartMapper.selectOrderCartItemsById(cartNos);

            List<Long> productNos = cartItems.stream()
                    .map(CartDto::getProdNo)
                    .collect(Collectors.toList());


            List<ProductPriceDto> productPriceList = productService.selectProductPrice(productNos);

            // 이걸 Map으로 변환
            Map<Long, Integer> productPriceMap = productPriceList.stream()
                    .collect(Collectors.toMap(ProductPriceDto::getProdNo, ProductPriceDto::getPrice));


            // 주문 생성
            CartDto cartItem = cartItems.get(0);
            OrderDto order = new OrderDto();
            order.setOrderDate(LocalDate.now());
            int totalAmount = cartItems.stream().mapToInt(CartDto::getQty).sum();
            order.setTotalAmount(totalAmount);
            order.setUserId(cartItem.getUserId());
//            order.setOrderStatus("ORDERED");


            // 주문 상세 생성
            List<OrderDetailDto> orderDetails = cartItems.stream().map(cart -> {
                OrderDetailDto detail = new OrderDetailDto();
                detail.setProdNo(cart.getProdNo());
                detail.setQty(cart.getQty());
                detail.setPrice(productPriceMap.get(cart.getProdNo()));
                return detail;
            }).collect(Collectors.toList());

            // 최종 래핑해서 반환


            result.put("orderMaster", order);
            result.put("orderDetails", orderDetails);
            result.put("cartNos", cartItems);
            return result;

        } catch (Exception e) {
            logger.info("CartService Error" + e.getMessage());
            return result;
        }

    }

    public ChkCartItemDto chkCartItem(Long cartNo) {
        return cartMapper.chkCartItem(cartNo);
    }

    public void deleteCart(Long cartNo){
        cartMapper.deleteCart(cartNo);
    }

}
//
//    public OrderRequestDto convertCartNosToOrderRequest(List<Long> cartNos) {
//        List<CartDto> cartItems = cartMapper.selectOrderCartItemsById(cartNos);
//
//        // 주문 마스터 생성
//        CartDto first = cartItems.get(0);
//        OrderDto orderMaster = new OrderDto();
//        orderMaster.setUserId(first.getUserId());
//        orderMaster.setOrderDate(LocalDate.now());
//        orderMaster.setOrderStatus("ORDERED");
//        int totalAmount = cartItems.stream()
//                .mapToInt(CartDto::getTotalPrice)
//                .sum();
//        orderMaster.setTotalAmount(totalAmount);
//
//        // 주문 상세 생성
//        List<OrderDetailDto> orderDetails = cartItems.stream().map(cart -> {
//            OrderDetailDto detail = new OrderDetailDto();
//            detail.setProductId(cart.getProductId());
//            detail.setQty(cart.getQty());
//            detail.setPrice(cart.getPrice());
//            return detail;
//        }).collect(Collectors.toList());
//
//        // 최종 래핑해서 반환
//        OrderRequestDto request = new OrderRequestDto();
//        request.setOrderMaster(orderMaster);
//        request.setOrderDetails(orderDetails);
//
//        return request;
//    }

//
//public List<OrderDto> convertCartNosToOrderItems(@Param("cartNos") List<Long> cartNos) {
//    // 1. 장바구니 상품 조회
//    List<CartDto> cartItems = cartMapper.selectOrderCartItemsById(cartNos);
//
//    // 2. 장바구니에서 상품번호만 추출
//    List<Long> productNos = cartItems.stream()
//            .map(CartDto::getProdNo)
//            .collect(Collectors.toList());
//
//    // 3. 상품 가격 일괄 조회 (상품번호 -> 가격)
//    Map<Long, Integer> productPriceMap = productService.selectProductPriceBatch(productNos);
//
//    // 4. 주문 마스터 생성
//    CartDto cartItem = cartItems.get(0);
//    OrderDto order = new OrderDto();
//    order.setOrderDate(LocalDate.now());
//    int totalAmount = cartItems.stream()
//            .mapToInt(cart -> productPriceMap.get(cart.getProdNo()) * cart.getQty()) // 총 금액은 수량 * 가격으로
//            .sum();
//    order.setTotalAmount(totalAmount);
//    order.setUserId(cartItem.getUserId());
//    order.setOrderStatus("ORDERED");
//
//    // 5. 주문 상세 생성
//    List<OrderDetailDto> orderDetails = cartItems.stream().map(cart -> {
//        OrderDetailDto detail = new OrderDetailDto();
//        detail.setProdNo(cart.getProdNo());
//        detail.setQty(cart.getQty());
//        detail.setPrice(productPriceMap.get(cart.getProdNo())); // 여기서 가격 매칭
//        return detail;
//    }).collect(Collectors.toList());
//
//    // 6. 결과 묶어서 반환
//    order.setOrderDetails(orderDetails);
//    return List.of(order);
