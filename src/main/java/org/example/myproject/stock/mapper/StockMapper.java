package org.example.myproject.stock.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.myproject.stock.dto.StockQtyDto;

import java.util.List;

@Mapper
public interface StockMapper {

    List<StockQtyDto> selectStockQty(List<Long> prodNo);
}
