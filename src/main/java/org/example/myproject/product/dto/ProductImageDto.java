package org.example.myproject.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageDto {
    Long prodNo;
    String imageUrl;
//    String isMain;  // Y/N
    Boolean isMain;  // 0/1
    int sortOrder;
}
