package org.example.myproject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.order.dto.OrderInfoDto;
import org.example.myproject.order.dto.OrderInfoProductDto;
import org.example.myproject.order.mapper.OrderMapper;
import org.example.myproject.product.dto.ProductImageDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootTest
public class TEST {

    @Autowired
    OrderMapper orderMapper;

    private static final Logger logger = LogManager.getLogger(TEST.class);

    @Test
    @WithMockUser("ANON-7004e94b-327e-4ebe-ba4a-eeffb6ea30f0")
    @DisplayName("그냥 출력 테스트")
    void outputTest() {

        List<ProductImageDto> productImageDto = List.of(
                ProductImageDto.builder().prodNo(1L).build(),
                ProductImageDto.builder().prodNo(2L).build()
        );

        List<Long> prodNos = productImageDto.stream().map(ProductImageDto::getProdNo).toList();

        List<OrderInfoDto> orderInfoDtos = orderMapper.selectOrderInfo("ORD20251111-2603", "ANON-7004e94b-327e-4ebe-ba4a-eeffb6ea30f0");
        logger.info("orderInfoDtos : {} ", orderInfoDtos);

        List<ProductImageDto> orderInfoProductDtos = orderMapper.selectOrdProdImage(prodNos);
        logger.info("orderInfoProductDtos : {} ", orderInfoProductDtos);
    }
}
