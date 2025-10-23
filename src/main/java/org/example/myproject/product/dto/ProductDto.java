package org.example.myproject.product.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ProductDto {
        private Long prodNo;
        private String prodName;
        private Integer price;
        private Integer stockQty;
        private String status;
        private LocalDate salesDt;

        private LocalDate crtDt;
        private LocalDate updDt;
        private String delYn;
        private LocalDate delDt;

        private String imageUrl;

}
