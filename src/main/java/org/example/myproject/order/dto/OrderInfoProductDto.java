package org.example.myproject.order.dto;

import lombok.Data;
import org.example.myproject.product.dto.ProductDetailDto;
import org.example.myproject.product.dto.ProductDto;

import java.time.LocalDate;
import java.util.List;


// 최적화 차원에서 기존 proudctDTO 와 별도의 OrderResult용 DTO로 분리.
@Data
public class OrderInfoProductDto {
    private Long prodNo;
    private String prodName;
    private String sellerId;
    private Integer price;
    private String status;
//    private LocalDate salesDt; 굳이 상품 판매일자가 여기에 필요할까?

    private String delYn;

    // 확장성 고려해서 단순 imageUrl (isMain = 1)가 아니라 subImage까지 가져오는 방식으로 하자.
    List<ProductDetailDto> ImageList;

}
