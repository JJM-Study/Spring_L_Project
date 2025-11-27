package org.example.myproject.stock.service;

import org.example.myproject.error.BusinessException;
import org.example.myproject.error.ErrorCode;
import org.example.myproject.order.dto.OrderDetailDto;
import org.example.myproject.stock.dto.StockQtyDto;
import org.example.myproject.stock.mapper.StockMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;

import java.util.List;
import java.util.Map;

@Service
public class StockService {

    @Autowired
    StockMapper stockMapper;

    //public List<StockQtyDto> selectStockQty(List<Long> prodNo) {
    public List<StockQtyDto> selectStockQty(Map<Long, Integer> requestQuantities) {
        return stockMapper.selectStockQty(requestQuantities);
    }


    @Transactional
    public void decreaseStock(Long prodNo, Integer stock) {

        Integer result = stockMapper.decreaseStock(prodNo, stock);

        if (result <= 0) {
            throw new BusinessException(ErrorCode.STOCK_NOT_ENOUGH);
        }

    }


    public Integer decreaseStockBulk(List<OrderDetailDto> orderDetailDto) {


        return stockMapper.decreaseStockBulk(orderDetailDto);

        //Integer updatedRows = stockMapper.decreaseStockBulk(orderDetailDto);
        // 차후 필요하면 받은 updatedRows로 처리 로직을 추가든지 할 것.

    }


    /* 임시 테스트용 */
    public void updateProdStock(StockQtyDto stock) {
        stockMapper.updateProdStock(stock);
    }

}
