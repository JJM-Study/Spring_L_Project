package org.example.myproject.order.service;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.mapper.AuthMapper;
import org.example.myproject.auth.service.AuthService;
import org.example.myproject.cart.dto.CartDto;
import org.example.myproject.cart.mapper.CartMapper;
import org.example.myproject.error.BusinessException;
import org.example.myproject.error.ErrorCode;
import org.example.myproject.order.dto.OrderListDTO;
import org.example.myproject.order.mapper.OrderMapper;
import org.example.myproject.order.mapper.OrderSequenceMapper;
import org.example.myproject.order.dto.OrderDetailDto;
import org.example.myproject.order.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.example.myproject.stock.dto.StockQtyDto;
import org.example.myproject.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    OrderSequenceMapper orderSequenceMapper;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    OrderNumberGeneratorService orderNumberGeneratorService;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    AuthService authService;

    @Autowired
    StockService stockService;

    private static final Logger logger = LogManager.getLogger(OrderService.class);


    // 1. createOrder 설계 미스. 즉시 주문, 카트 주문 동시 사용하게 설계했으나 단일 주문은 단독 주문이라는 걸 고려하지
    // 못하여 파라미터를 List.of로 굳이 바꿔서 createOrder 보내는 수고를 해야 함. 이에 대해서 차후 리펙토링 필요하다고 생각.
    // 2. 지금 보니 orderMaster 파라미터는 불필요하다. 장바구니가 안정화 되면, 제거하는 방향으로 리펙토링.
    // 3. 결제 도입 시엔 현재 메소드는 먼저 결제 후 주문하는 preCreateOrder로 리펙토링하고, conform 메소드를 따로 만들어 처리하는 방법으로 고민. 선 결제 시에도 OrderCrate 후 상태값을 남김. -> 결제 완료 상태일 경우 conform에서 바로 처리.
    @Transactional
//    public String createOrder(OrderDto orderMaster, List<OrderDetailDto> orderDetails) {
    public String createOrder(OrderDto orderMaster, List<OrderDetailDto> orderDetails, @Nullable List<CartDto> cartDto, HttpServletRequest request) {

        Map<Long, Integer> requestQuantities = orderDetails.stream()
                .collect(Collectors.toMap(
                        OrderDetailDto::getProdNo,
                        OrderDetailDto::getQty
                ));

// DB 조회에 필요한 상품 번호 리스트
        //List<Long> prodNos = new ArrayList<>(requestQuantities.keySet());
        List<StockQtyDto> stock = stockService.selectStockQty(requestQuantities);


        //List<StockQtyDto> stock = stockService.selectStockQty(prodNos, requestQty);

        List<String> notEnoughProdNames = stock.stream()
                .filter(dto -> {

                    Integer requestedQty = requestQuantities.get(dto.getProdNo());

                    return dto.getStockQty() < requestedQty;
                })
                .map(StockQtyDto::getProdName)
                .toList();



        if (!notEnoughProdNames.isEmpty()) {

            String productNames = String.join(", ", notEnoughProdNames);

            throw new BusinessException(ErrorCode.STOCK_NOT_ENOUGH_DETAIL, productNames);
        }

        if (orderMaster == null) {
//            throw new IllegalArgumentException("주문 상품이 비어 있습니다.");
            throw new BusinessException(ErrorCode.PRODUCT_ORDER_NOT_FOUND);
        }

        String orderNo = orderNumberGeneratorService.generateOrderNumber();

        // 주문 처리 여기에 추가할 것. insertOrderMaster(OrderDto order);
        // 주문 처리 여기에 추가할 것. insertOrderDetail(OrderDto orderDetail);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId = authService.getAuthenticUserId(request);

//        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetails)) {
//            logger.info("authentication: " + authentication);
//            logger.info("!authentication.isAuthenticated() : " + !authentication.isAuthenticated());
//            logger.info("!(authentication instanceof UserDetails) " + !(authentication instanceof UserDetails));
//
//            //throw new RuntimeException("로그인 필요");
//            throw new BusinessException(ErrorCode.REQUEST_LOGIN);
//        }
//
//
//
//
//        Object principal = authentication.getPrincipal();
//        if (principal instanceof UserDetails) {
//            userId = ((UserDetails)authentication.getPrincipal()).getUsername();
//            logger.info("userId :" + userId);
//        } else {
//            throw new RuntimeException("사용자 정보 확인 불가.");
//        }

        orderMaster.setUserId(userId);
        orderMaster.setOrderNo(orderNo);
        logger.info("orderMaster :" + orderMaster);

        orderMapper.insertOrderMaster(orderMaster);


//        for (OrderDetailDto details : orderDetails) {
//            details.setOrderNo(orderNo);
//            orderMapper.insertOrderDetail(details);
//            stockService.decreaseStock(details.getProdNo(), details.getQty());
//        }

        orderDetails.forEach((details -> details.setOrderNo(orderNo)));


        orderMapper.insertOrderDetailBulk(orderDetails);

        stockService.decreaseStockBulk(orderDetails); // for문 안 쓰고 좀 더 효율적으로 일괄 처리하도록 리팩토링 함. 2025/11/04

        /*=== 통합 테스트용 ===*/
        List<StockQtyDto> stockQtyDtos = stockService.selectStockQty(requestQuantities);

        logger.info("차감 후 재고 : " + stockQtyDtos.get(0).getProdName() + " = 남은 수량 : " + stockQtyDtos.get(0).getStockQty());
        /*========================*/


//        for (CartDto cartNos : cartDto) {
//            Long cartNo = cartNos.getCartNo();
//            cartMapper.deleteCart(cartNo);
//        }
        if (cartDto != null && !cartDto.isEmpty()) {
            List<Long> cartNos = cartDto.stream().map(CartDto::getCartNo).toList();

            cartMapper.deleteCartBulk(cartNos);
        }

        return orderNo;
    }


    //public List<OrderListDTO> orderList(int pageSize, int offset) {
    public List<OrderListDTO> orderList(int pageSize, int offset, String userId) {
        //return orderMapper.orderList(pageSize, offset);
        return orderMapper.orderList(pageSize, offset, userId);
    }

    public int selectOrdListCount(String userId) {
        return orderMapper.selectOrdListCount(userId);
    };

    // 주문번호 조회
    public String selectOrderNum(String orderNo) {
        return orderMapper.selectOrderNum(orderNo);
    }

}
