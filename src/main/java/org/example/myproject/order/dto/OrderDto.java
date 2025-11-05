package org.example.myproject.order.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

//    private String orderDtFormatted;

}
