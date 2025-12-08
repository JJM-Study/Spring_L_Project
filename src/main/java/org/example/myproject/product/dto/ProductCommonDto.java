package org.example.myproject.product.dto;

import lombok.*;

import java.util.List;

public class ProductCommonDto {
    // 수량 업데이트
    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class QtyUpdate {
        private Long prodNo;
        private Integer qty;
        //private String reason; // 변경 사유. 나중에 사용할 지 고민.
    }

    // 가격 조회
    @Getter @Builder @AllArgsConstructor @NoArgsConstructor
    public static class PriceInfo {
        private Long prodNo;
        private Integer price;
    }

    // 상품 이미지 조회
    @Data @Builder @AllArgsConstructor @NoArgsConstructor
    public static class ImageGroup {
        private ProductImageDto mainImage;
        private List<ProductImageDto> subImages;
    }

}
