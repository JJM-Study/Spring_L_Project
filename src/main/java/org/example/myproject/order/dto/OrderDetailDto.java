package org.example.myproject.order.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {
    private Long orderDetailNo;
    private String orderNo;
    private Long prodNo;
    private Integer qty;
    private Integer price;
    private Boolean delYN;
    private LocalDate crtDt;
    private LocalDate updDt;
    private LocalDate delDt;
}
