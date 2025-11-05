package org.example.myproject;


import jakarta.servlet.http.HttpServletRequest;
import org.example.myproject.cart.dto.CartDto;
import org.example.myproject.cart.mapper.CartMapper;
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
import org.springframework.boot.test.context.SpringBootTest;
import org.slf4j.Logger;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;

//import static jdk.internal.org.jline.reader.impl.LineReaderImpl.CompletionType.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.when;
//
//import java.util.logging.LogManager;
//import java.util.logging.Logger;


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


        String orderId = orderService.createOrder(orderMaster ,orderDetails, cartList, mockHttpServletRequest);

        logger.info("orderId : " + orderId);


        //orderService.createOrder(1L, 81);

    }


    @Autowired
    CartMapper cartMapper;

    @Test
    @WithMockUser("MockTester1")
    @DisplayName("장바구니 삭제 권한에 따른 예외 처리")
    void DeleteAccessTestExceptionTest() {
        logger.info("UserId Missmatching 에 따른 예외 검사 Start : ");


        Long cartNo = 1L;
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

        cartMapper.deleteCart(cartNo);

    }


}
