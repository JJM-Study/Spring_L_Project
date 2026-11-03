package org.example.myproject.shop.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
public class ShopDTO {
    private String shopId;
    private String shopName;
    private String categoryLarge;
    private String categoryMid;
    private String roadAddress;

}
