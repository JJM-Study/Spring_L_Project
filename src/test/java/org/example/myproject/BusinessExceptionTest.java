package org.example.myproject;


import jakarta.servlet.http.HttpServletRequest;
import org.assertj.core.api.Assertions;
import org.example.myproject.auth.mapper.AuthMapper;
import org.example.myproject.auth.service.AuthService;
import org.example.myproject.cart.dto.CartDto;
import org.example.myproject.cart.dto.ChkCartItemDto;
import org.example.myproject.cart.mapper.CartMapper;
import org.example.myproject.cart.service.CartService;
import org.example.myproject.error.BusinessException;
import org.example.myproject.error.ErrorCode;
import org.example.myproject.order.dto.OrderDetailDto;
import org.example.myproject.order.dto.OrderDto;
import org.example.myproject.order.mapper.OrderMapper;
import org.example.myproject.order.service.OrderNumberGeneratorService;
import org.example.myproject.order.service.OrderService;
import org.example.myproject.stock.mapper.StockMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.util.Assert;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;

//import static jdk.internal.org.jline.reader.impl.LineReaderImpl.CompletionType.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
//
//import java.util.logging.LogManager;
//import java.util.logging.Logger;

// 동시성 테스트 고민.

@SpringBootTest
public class BusinessExceptionTest {

    private static Logger logger = LoggerFactory.getLogger(OrderServiceRollbackTest.class);

    @Autowired
    StockMapper stockMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderService orderService;

    @Mock
    OrderNumberGeneratorService orderNumberGeneratorService;

    @Test
    @DisplayName("비지니스 예외 테스트 scenario 1: 재고 부족")
    @WithMockUser("MockTester1")
    void StockExceptionTest() {

        Map<Long, Integer> Map_Product_QTY = Map.of(
                1L, 90,
                2L, 100,
                3L, 70,
                4L, 80,
                5L, 100
        );


        final String MOKED_ORDER_NO = "ORD20251103-0011";

        logger.info("재고 부족 예외 테스트 Start : ");
        when(orderNumberGeneratorService.generateOrderNumber()).thenReturn(MOKED_ORDER_NO);


        OrderDto orderMaster = OrderDto.builder().build();
        List<CartDto> cartList = List.of(
                                         CartDto.builder().cartNo(1L).build(),
                                         CartDto.builder().cartNo(2L).build(),
                                         CartDto.builder().cartNo(3L).build(),
                                         CartDto.builder().cartNo(4L).build(),
                                         CartDto.builder().cartNo(5L).build()
        );


        Integer orderCount = orderMapper.selectCountByOrderNo(MOKED_ORDER_NO);


        List<OrderDetailDto> orderDetails = Map_Product_QTY.entrySet().stream()
                .map(entry -> OrderDetailDto.builder()
                        .prodNo(entry.getKey())
                        .qty(entry.getValue())
                        .build()
                ).collect(Collectors.toList());

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();


        BusinessException exception = assertThrows(BusinessException.class, () -> {
            orderService.createOrder(orderMaster, orderDetails, cartList, mockHttpServletRequest);
        });


        assertEquals(ErrorCode.STOCK_NOT_ENOUGH_DETAIL, exception.getErrorCode());




        logger.info("Exception ErrorCode: " + exception.getErrorCode());
        logger.info("Exception Message: " + exception.getMessage());
//        String orderId = orderService.createOrder(orderMaster ,orderDetails, cartList, mockHttpServletRequest);
//


//        logger.info("orderId : " + orderId);


        //orderService.createOrder(1L, 81);

    }

// 다음엔 PRODUCT_ORDER_NOT_FOUND (주문 상품이 비어있습니다 O002) 정도 검증하는 게 좋을 듯.


}
