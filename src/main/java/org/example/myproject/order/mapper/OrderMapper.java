package org.example.myproject.order.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.myproject.order.dto.OrderDetailDto;
import org.example.myproject.order.dto.OrderDto;
import org.apache.ibatis.annotations.Mapper;
import org.example.myproject.order.dto.OrderListDTO;

import java.util.List;

@Mapper
public interface OrderMapper {

    void insertOrderMaster(@Param("order") OrderDto order);
    void insertOrderDetail(@Param("order") OrderDetailDto orderDetail);

    List<OrderListDTO> orderList(@Param("pageSize") int pageSize, @Param("offset") int offset);

    int selectOrdListCount();
}
