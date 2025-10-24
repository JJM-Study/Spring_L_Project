package org.example.myproject.order.service;

import jakarta.annotation.Nullable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


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

    private static final Logger logger = LogManager.getLogger(OrderService.class);


    // 1. createOrder 설계 미스. 즉시 주문, 카트 주문 동시 사용하게 설계했으나 단일 주문은 단독 주문이라는 걸 고려하지
    // 못하여 파라미터를 List.of로 굳이 바꿔서 createOrder 보내는 수고를 해야 함. 이에 대해서 차후 리펙토링 필요하다고 생각.
    // 2. 지금 보니 orderMaster 파라미터는 불필요하다. 장바구니가 안정화 되면, 제거하는 방향으로 리펙토링.
    @Transactional
//    public String createOrder(OrderDto orderMaster, List<OrderDetailDto> orderDetails) {
    public String createOrder(OrderDto orderMaster, List<OrderDetailDto> orderDetails, @Nullable List<CartDto> cartDto) {

        if (orderMaster == null) {
//            throw new IllegalArgumentException("주문 상품이 비어 있습니다.");
            throw new BusinessException(ErrorCode.PRODUCT_ORDER_NOT_FOUND);
        }

        String orderNo = orderNumberGeneratorService.generateOrderNumber();

        // 주문 처리 여기에 추가할 것. insertOrderMaster(OrderDto order);
        // 주문 처리 여기에 추가할 것. insertOrderDetail(OrderDto orderDetail);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId;

        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetails)) {
            logger.info("authentication: " + authentication);
            logger.info("!authentication.isAuthenticated() : " + !authentication.isAuthenticated());
            logger.info("!(authentication instanceof UserDetails) " + !(authentication instanceof UserDetails));

            //throw new RuntimeException("로그인 필요");
            throw new BusinessException(ErrorCode.REQUEST_LOGIN);
        }




        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            userId = ((UserDetails)authentication.getPrincipal()).getUsername();
            logger.info("userId :" + userId);
        } else {
            throw new RuntimeException("사용자 정보 확인 불가.");
        }

        orderMaster.setUserId(userId);
        orderMaster.setOrderNo(orderNo);
        logger.info("orderMaster :" + orderMaster);

        orderMapper.insertOrderMaster(orderMaster);


        for (OrderDetailDto details : orderDetails) {
            details.setOrderNo(orderNo);
            orderMapper.insertOrderDetail(details);
        }

        for (CartDto cartNos : cartDto) {
            Long cartNo = cartNos.getCartNo();
            cartMapper.deleteCart(cartNo);
        }

        return orderNo;
    }


    public List<OrderListDTO> orderList(int pageSize, int offset) {
        return orderMapper.orderList(pageSize, offset);
    }

    public int selectOrdListCount() {
        return orderMapper.selectOrdListCount();
    };

}
