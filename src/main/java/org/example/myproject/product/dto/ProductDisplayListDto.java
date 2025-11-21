package org.example.myproject.product.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
public class ProductDisplayListDto {
    private Long salesCount;
    private Double rating;
    private LocalDateTime salesDt;
    private Long prodNo;
    private String prodName;
    private String prodType;
    private Integer price;
    private String sellerId;
    private String imageUrl;
    private Boolean isInCart;
    private Boolean isInLyb;

    private String listType;


    public String getSalesDtStr() {
        if (salesDt == null) return "";

        return salesDt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
