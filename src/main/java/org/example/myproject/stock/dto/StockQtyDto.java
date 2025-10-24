package org.example.myproject.stock.dto;

import lombok.Data;

@Data
public class StockQtyDto {
    private Long prodNo;
    private String prodNmae;
    private Integer stockQty;
}
