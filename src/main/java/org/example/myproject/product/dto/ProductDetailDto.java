package org.example.myproject.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDetailDto {
    private Long prodNo;
    private String prodName;

    private String prodType;

    private String sellerId;

    private Integer price;
    private Integer stockQty;

    private LocalDateTime salesDt;

    private LocalDateTime crtDt;
    private LocalDateTime updDt;
    private String delYn;
    private LocalDateTime delDt;

    private String detailDesc;
    private String notice;
    private String shippingInfo;
    private String additionalInfo;

    private String salesDtFormatted;

    private Boolean isInLyb;

    private List<ProductImageDto> imageList;

}
