package org.example.myproject.order.dto;

import lombok.Data;
import org.example.myproject.product.dto.ProductDto;
import org.example.myproject.product.dto.ProductImageDto;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderInfoDto {
    private String orderNo;
    private String userId;
    private LocalDate orderDate;
    private String orderStatus; // Default = 'READY'
    private String paymentMethod;
    private Long totalPrice;
    private Integer totalAmount;
    private String delYn;

    private List<OrderInfoProductDto> ordInfoProdList;

//    private List<ProductImageDto> productImageDtoList;

}