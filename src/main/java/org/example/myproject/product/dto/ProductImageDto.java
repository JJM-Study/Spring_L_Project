package org.example.myproject.product.dto;

import lombok.Data;

@Data
public class ProductImageDto {
    String imageUrl;
    String isMain;  // Y/N
    int sortOrder;
}
