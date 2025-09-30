package org.example.myproject.order.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.cart.mapper.CartMapper;
import org.example.myproject.order.mapper.OrderMapper;
import org.example.myproject.order.mapper.OrderSequenceMapper;
import org.example.myproject.order.dto.OrderDetailDto;
import org.example.myproject.order.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
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

    @Transactional
    public String createOrder(OrderDto orderMaster, List<OrderDetailDto> orderDetails) {

        if (orderMaster == null) {
            throw new IllegalArgumentException("주문 상품이 비어 있습니다.");
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

            throw new RuntimeException("로그인 필요");
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

        cartMapper.deleteCart();

        return orderNo;
    }



}
