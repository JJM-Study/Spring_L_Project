package org.example.myproject.order.service;

import org.example.myproject.order.mapper.OrderMapper;
import org.example.myproject.order.mapper.OrderSequenceMapper;
import org.example.myproject.order.dto.OrderDetailDto;
import org.example.myproject.order.dto.OrderDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    OrderSequenceMapper orderSequenceMapper;

    @Autowired
    OrderNumberGeneratorService orderNumberGeneratorService;

    @Autowired
    OrderMapper orderMapper;

    @Transactional
    public String createOrder(OrderDto orderMaster, List<OrderDetailDto> orderDetails) {

        if (orderMaster == null) {
            throw new IllegalArgumentException("주문 상품이 비어 있습니다.");
        }

        String orderNo = orderNumberGeneratorService.generateOrderNumber();

        // 주문 처리 여기에 추가할 것. insertOrderMaster(OrderDto order);
        // 주문 처리 여기에 추가할 것. insertOrderDetail(OrderDto orderDetail);
        orderMaster.setOrderNo(orderNo);

        orderMapper.insertOrderMaster(orderMaster);

        for (OrderDetailDto details : orderDetails) {
            details.setOrderNo(orderNo);
            orderMapper.insertOrderDetail(details);
        }

        return orderNo;
    }



}
