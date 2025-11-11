package org.example.myproject.order.mapper;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;
import org.example.myproject.order.dto.*;
import org.apache.ibatis.annotations.Mapper;
import org.example.myproject.product.dto.ProductImageDto;
import org.example.myproject.stock.dto.StockQtyDto;

import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    void insertOrderMaster(@Param("order") OrderDto order);
    void insertOrderDetail(@Param("order") OrderDetailDto orderDetail);

    //List<OrderListDTO> orderList(@Param("pageSize") int pageSize, @Param("offset") int offset);
    List<OrderListDTO> orderList(@Param("pageSize") int pageSize, @Param("offset") int offset, String userId);

    int selectOrdListCount(String userId);

    void insertOrderDetailBulk(List<OrderDetailDto> orderDetails);

    // 일단 테스트용
    int selectCountByOrderNo(String orderNo);

    List<OrderInfoDto> selectOrderInfo(String orderNo, String userId);

    List<StockQtyDto> productStockList(@Param("prodNos") List<Long> prodNos);

    String selectOrderNum(String orderNo);

    // 2025/11/11 수정 필요.
    List<ProductImageDto> selectOrdProdImage(@Param("list") List<Long> prodNo);

}
