package org.example.myproject;

import org.example.myproject.cart.dto.CartDto;
import org.example.myproject.cart.mapper.CartMapper;
import org.example.myproject.order.dto.OrderDetailDto;
import org.example.myproject.order.dto.OrderDto;
import org.example.myproject.order.mapper.OrderMapper;
import org.example.myproject.order.service.OrderNumberGeneratorService;
import org.example.myproject.order.service.OrderService;
import org.example.myproject.stock.dto.StockQtyDto;
import org.example.myproject.stock.mapper.StockMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.SqlSessionTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.anyList;


/**
 * 트랜잭션 원자성 검증을 위한 독립된 테스트 클래스입니다.
 * (다른 로직 실패 시 재고 차감이 롤백되는지 검증)
 */
@SpringBootTest
public class OrderServiceRollbackTest {

    // ★ 로거 추가
    private static final Logger logger = LoggerFactory.getLogger(OrderServiceRollbackTest.class);

    // 실제 빈 주입
    @Autowired
    OrderService orderService;
    @Autowired
    OrderMapper orderMapper;
    @Autowired
    StockMapper stockMapper;

    // Mock 빈 대체 (실패 주입 및 주문 번호 생성)
    @MockitoBean
    OrderNumberGeneratorService orderNumberGeneratorService;
    @MockitoSpyBean
    CartMapper cartMapper;

    @Autowired
    private SqlSessionTemplate sqlSessionTemplate;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @Test
    @DisplayName("다른 로직 실패 시 재고 차감이 롤백되어 원상복구되는지 검증")
    @WithMockUser(username = "testUser01")
    void testCreateOrder_RollbackOnOtherFailure() {

        // GIVEN: 테스트 데이터 준비 및 Mocking 설정
        final String MOKED_ORDER_NO = "ORD20251103-0011";
        when(orderNumberGeneratorService.generateOrderNumber()).thenReturn(MOKED_ORDER_NO);

        OrderDto orderMaster = OrderDto.builder().build();
        List<CartDto> cartList = List.of(CartDto.builder().cartNo(1L).build());
        MockHttpServletRequest mockRequest = new MockHttpServletRequest();
        Map<Long, Integer> targetStock = Map.of(1L, 81);

        // 1. ★ 별도 트랜잭션에서 초기 재고 조회 (명확한 로그)
        TransactionTemplate readTx = new TransactionTemplate(transactionManager);
        readTx.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

        Integer originalStockQty = readTx.execute(status -> {
            List<StockQtyDto> stockList = stockMapper.selectStockQty(targetStock);
            Integer qty = stockList.get(0).getStockQty();
            logger.info("=== [초기 재고 조회] 상품번호: {}, 재고: {} ===",
                    stockList.get(0).getProdNo(), qty);
            return qty;
        });

        // 2. 주문 상세 데이터 (재고 차감 성공 유도)
        List<OrderDetailDto> validOrderDetails = List.of(
                OrderDetailDto.builder().prodNo(1L).qty(81).build()
        );

        // 3. 가짜 예외 주입 (Fault Injection)
        doThrow(new RuntimeException("네트워크 세션 끊김 오류 가정"))
                .when(cartMapper).deleteCartBulk(anyList());

        // 4. ★ 주문 생성 로직 실행 (예외 발생 기대)
        logger.info("=== [주문 시작] 재고 {}개 차감 예정 ===", 81);
        Assertions.assertThrows(RuntimeException.class, () -> {
            orderService.createOrder(orderMaster, validOrderDetails, cartList, mockRequest);
        });
        logger.info("=== [예외 발생 확인] 트랜잭션 롤백 예정 ===");

        // 5. MyBatis 캐시 클리어
        sqlSessionTemplate.clearCache();

        // 6. ★ 별도의 새로운 트랜잭션에서 최종 재고 조회 (롤백 후 DB 실제 값 확인)
        Integer finalStockQty = readTx.execute(status -> {
            List<StockQtyDto> stockList = stockMapper.selectStockQty(targetStock);
            Integer qty = stockList.get(0).getStockQty();
            logger.info("=== [롤백 후 재고 조회] 상품번호: {}, 재고: {} ===",
                    stockList.get(0).getProdNo(), qty);
            return qty;
        });

        // 7. ★ Assertion (검증) - 자세한 메시지
        logger.info("=== [검증 시작] 초기재고: {}, 최종재고: {} ===",
                originalStockQty, finalStockQty);

        assertThat(finalStockQty)
                .as("다른 로직에서 예외 발생 시, 재고 차감이 롤백되어 원본 수치와 같아야 함")
                .isEqualTo(originalStockQty);

        logger.info("=== [검증 성공] 재고가 {}에서 {}로 정상 복구됨 ===",
                originalStockQty, finalStockQty);

        // 8. 별도 트랜잭션에서 주문 데이터 확인
        Integer orderCount = readTx.execute(status -> {
            Integer count = orderMapper.selectCountByOrderNo(MOKED_ORDER_NO);
            logger.info("=== [주문 데이터 확인] 주문번호 {} 존재 여부: {} (0이어야 함) ===",
                    MOKED_ORDER_NO, count);
            return count;
        });

        assertThat(orderCount).isZero();

        // CartMapper의 deleteCartBulk가 1회 호출되었는지 검증
        verify(cartMapper, times(1)).deleteCartBulk(anyList());

        logger.info("=== [테스트 완료] 모든 검증 통과 ===");
    }
}