package org.example.myproject.order.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.myproject.order.dto.OrderDetailDto;
import org.example.myproject.order.dto.OrderDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface OrderMapper {

    void insertOrderMaster(@Param("order") OrderDto order);
    void insertOrderDetail(@Param("order") OrderDetailDto orderDetail);

}
