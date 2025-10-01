package org.example.myproject.order.dto;

import lombok.Data;

@Data
public class OrderRequestDto {
    private Long prodNo;
    private Integer qty;
}
