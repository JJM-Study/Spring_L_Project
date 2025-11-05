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
import org.example.myproject.stock.service.StockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.StatusResultMatchersExtensionsKt.isEqualTo;

// 트랜잭션 통합 테스트용

// 주문 로직 원자성 검증.
@Transactional
@SpringBootTest
@Rollback(false)
public class OrderServiceTransactionTest {



    @Autowired
    OrderService orderService;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    StockService stockService;

    @Autowired
    CartMapper cartMapper;

    @Mock
    OrderNumberGeneratorService orderNumberGeneratorService;


    private MockHttpServletRequest mockRequest;

    @Test
    @DisplayName("재고 부족 시 주문 트랜잭션 전체 롤백 검증")
    @WithMockUser(username = "testUser01")
//    @WithAnonymousUser // 익명 테스트
    void testCreateOrder_StockNotEnough() {



        final String MOKED_ORDER_NO = "ORD20251103-0010";

        // GIVEN
        List<OrderDetailDto> insufficientOrderDetails = List.of(
                OrderDetailDto.builder().prodNo(1L).qty(81).build()
        );

        when(orderNumberGeneratorService.generateOrderNumber()).thenReturn(MOKED_ORDER_NO);

        OrderDto orderMaster = OrderDto.builder().build();
        List<CartDto> cartList = List.of(CartDto.builder().cartNo(1L).build());

        MockHttpServletRequest mockRequest = new MockHttpServletRequest();

        assertThatThrownBy(() -> {
            orderService.createOrder(orderMaster, insufficientOrderDetails, cartList, mockRequest);
        })
        .isInstanceOf(BusinessException.class)
        .extracting("errorCode")
        .isEqualTo(ErrorCode.STOCK_NOT_ENOUGH_DETAIL);

        // 테이블 데이터 생성 여부 확인.
        assertThat(orderMapper.selectCountByOrderNo(MOKED_ORDER_NO)).isZero();

        Map<Long, Integer> qty = Map.of(1L, 81);

        // 재고 수량 변경 여부 확인.
        assertThat(stockService.selectStockQty(qty).get(0).getStockQty()).isEqualTo(81); // 원래 재고로 완벽히 돌아왔는가

        // 장바구니 항목이 삭제되지 않았는지 여부 확인.
        assertThat(cartMapper.selectCartCount(2)).isEqualTo(2);

    }

}