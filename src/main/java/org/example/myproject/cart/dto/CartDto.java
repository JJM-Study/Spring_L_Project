package org.example.myproject.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    private Long cartNo;
    private String userId;
    private Long prodNo;
    private String prodName;
    private String ordNo;
    private Integer qty;
    private LocalDate crtDt;
    private LocalDate updDt;
    private LocalDate delDt;
    private String delYn;

}
