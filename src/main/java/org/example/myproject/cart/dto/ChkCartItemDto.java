package org.example.myproject.cart.dto;

import lombok.Data;

@Data
public class ChkCartItemDto {
    private int cartNo;
    private String userId;
    private Long prodNo;
}
