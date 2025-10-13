package org.example.myproject.product.dto;

import java.util.List;

import lombok.Data;

@Data
public class SendImageDTO {
    private ProductImageDto mainImage;
    private List<ProductImageDto> subImages;
}
