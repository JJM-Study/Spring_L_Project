package org.example.myproject.order.mapper;

import org.example.myproject.order.dto.OrderDetailDto;
import org.example.myproject.order.dto.OrderDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    void insertOrderMaster(OrderDto order);
    void insertOrderDetail(OrderDetailDto orderDetail);
}
