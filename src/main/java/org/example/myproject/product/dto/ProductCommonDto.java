package org.example.myproject.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ProductCommonDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QtyUpdate { // 수량 업데이트용
        private Long prodNo;
        private Integer qty;
        //private String reason; // 변경 사유. 나중에 사용할 지 고민.
    }
}
