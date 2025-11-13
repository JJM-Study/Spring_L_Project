package org.example.myproject.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.myproject.product.enums.ProductType;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StockQtyDto {
    private Long prodNo;
    private String prodName;
    private Integer stockQty;
    private Integer requestQty;
    private ProductType prodType;
}
