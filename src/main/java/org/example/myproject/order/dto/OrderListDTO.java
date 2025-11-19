package org.example.myproject.order.dto;

import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class OrderListDTO {
    private String orderNo;
    //private LocalDateTime orderDate;
    private String orderDate;
    private String prodName;
    private String prodType;

//    private String orderDtFormatted;
}
