package org.example.myproject.stock.service;

import org.example.myproject.error.BusinessException;
import org.example.myproject.error.ErrorCode;
import org.example.myproject.stock.dto.StockQtyDto;
import org.example.myproject.stock.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class StockService {

    @Autowired
    StockMapper stockMapper;

    public List<StockQtyDto> selectStockQty(List<Long> prodNo) {
        return stockMapper.selectStockQty(prodNo);
    }


    @Transactional
    public void decreaseStock(Long prodNo, Integer stock) {

        Integer result = stockMapper.decreaseStock(prodNo, stock);

        if (result <= 0) {
            throw new BusinessException(ErrorCode.STOCK_NOT_ENOUGH);
        }

    }

}
