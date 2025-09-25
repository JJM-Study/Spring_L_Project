package org.example.myproject.order.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class OrderDto {
    private String orderNo;
    private String userId;
    private LocalDate orderDate;
    private String orderStatus; // Default = 'READY'
    private Integer totalAmount;
    private String delYn;
    private LocalDate crtDt;
    private LocalDate updDt;
    private LocalDate delDt;

}
