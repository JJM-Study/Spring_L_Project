package org.example.myproject.product.dto;

import lombok.Data;

@Data
public class ProductImageDto {
    String imageUrl;
//    String isMain;  // Y/N
    Boolean isMain;  // 0/1
    int sortOrder;
}
