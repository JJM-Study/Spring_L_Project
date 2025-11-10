package org.example.myproject.stock.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.myproject.order.dto.OrderDetailDto;
import org.example.myproject.stock.dto.StockQtyDto;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Mapper
public interface StockMapper {

    //List<StockQtyDto> selectStockQty(List<Long> prodNo);
    List<StockQtyDto> selectStockQty(@Param("requestMap") Map<Long, Integer> requestQuantities);

    Integer decreaseStock(Long prodNo, Integer stockQty);

    void decreaseStockBulk(List<OrderDetailDto> orderDetailDto);


    // 임시 테스트용
    void updateProdStock(@Param("stock") StockQtyDto stock);

}
