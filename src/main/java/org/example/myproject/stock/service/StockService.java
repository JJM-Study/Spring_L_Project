package org.example.myproject.stock.service;

import org.example.myproject.stock.dto.StockQtyDto;
import org.example.myproject.stock.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockService {

    @Autowired
    StockMapper stockMapper;

    public List<StockQtyDto> selectStockQty(List<Long> prodNo) {
        return stockMapper.selectStockQty(prodNo);
    }
}
