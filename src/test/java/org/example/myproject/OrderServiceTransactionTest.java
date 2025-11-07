package org.example.myproject;

import org.example.myproject.cart.dto.CartDto;
import org.example.myproject.cart.mapper.CartMapper;
import org.example.myproject.error.BusinessException;
import org.example.myproject.error.ErrorCode;
import org.example.myproject.order.dto.OrderDetailDto;
import org.example.myproject.order.dto.OrderDto;
import org.example.myproject.order.mapper.OrderMapper;
import org.example.myproject.order.service.OrderNumberGeneratorService;
import org.example.myproject.order.service.OrderService;
import org.example.myproject.stock.dto.StockQtyDto;
import org.example.myproject.stock.mapper.StockMapper;
import org.example.myproject.stock.service.StockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

// 트랜잭션 통합 테스트용

// 주문 로직 원자성 검증.



@SpringBootTest
public class OrderServiceTransactionTest {

    private static Logger logger = LoggerFactory.getLogger(OrderServiceTransactionTest.class);

    @Autowired
    OrderService orderService;

    @Autowired
    StockMapper stockMapper;

    @MockBean
    CartMapper cartMapper;

    @MockBean
    StockService StockService;

    @MockBean
    OrderNumberGeneratorService orderNumberGeneratorService;

    @Test
    @WithMockUser("mockStockTester")
    @DisplayName("주문 도중 오류 시 재고 롤백 테스트")
    void stockRollTest() {

        final String MockedOrderNo = "ORD20251103-0011";

        Long prodNo = 1L;
        Integer qty = 80;
        //Integer requiredQty = 100;

        //Map<Long, Integer> prodQty = Map.of(prodNo, qty);

        OrderDto order = OrderDto.builder().build();
        List<OrderDetailDto> orderDetailDtoList = List.of(OrderDetailDto.builder().prodNo(prodNo).qty(qty).build());

        List<StockQtyDto> stockQty = stockMapper.selectStockQty(Map.of(prodNo, qty));

        logger.info("====== 롤백 테스트 시작 : ======");
        logger.info("차감 전 재고 : " + stockQty.get(0).getProdName() + " = 남은 수량 : " + stockQty.get(0).getStockQty());


        logger.info("====== 차감 시작 : ======");
        stockMapper.decreaseStockBulk(orderDetailDtoList);
        stockQty = stockMapper.selectStockQty(Map.of(prodNo, qty));
        logger.info("차감 후 재고 : " + stockQty.get(0).getProdName() + " = 남은 수량 : " + stockQty.get(0).getStockQty());


        when(orderNumberGeneratorService.generateOrderNumber())
                .thenReturn(MockedOrderNo);



        OrderDto orderDto = new OrderDto();

//        doThrow(new RuntimeException("네트워크 끊김 가정"))
//                .when(cartMapper).deleteCartBulk();
        doThrow(new RuntimeException()).when(cartMapper).deleteCartBulk(anyList());

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();


        Throwable thrown = catchThrowable(() ->
            orderService.createOrder(order, orderDetailDtoList, null, mockHttpServletRequest)
        );


    }
}