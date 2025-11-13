package org.example.myproject.product.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;
import org.example.myproject.product.enums.ProductType;

import java.time.LocalDate;

@Data
public class ProductDto {
        private Long prodNo;
        private String prodName;

        private String sellerId;

        private Integer price;
        private Integer stockQty;
        private String prodStatus;


        private ProductType prodType;

        private LocalDate salesDt;

        private LocalDate crtDt;
        private LocalDate updDt;
        private String delYn;
        private LocalDate delDt;

        private String imageUrl;

        private Boolean isInCart;

}
