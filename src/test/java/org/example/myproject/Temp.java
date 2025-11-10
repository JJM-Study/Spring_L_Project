package org.example.myproject;

import org.example.myproject.error.BusinessException;
import org.example.myproject.order.dto.OrderDetailDto;
import org.example.myproject.order.dto.OrderDto;
import org.example.myproject.order.service.OrderService;
import org.example.myproject.product.service.ProductService;
import org.example.myproject.stock.service.StockService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@SpringBootTest
public class Temp {

    @Autowired
    private OrderService orderService;
    @Autowired
    private StockService stockService;
    @Autowired
    private ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(Temp.class);

    private final int THREAD_COUNT = 100;
    private final Long TEST_PROD_NO = 100L;
    private final int INITIAL_STOCK_QTY = 1200;

    private final AtomicInteger successCount = new AtomicInteger(0);
    private final AtomicInteger failCount = new AtomicInteger(0);

    @BeforeEach
    void setUp() {
        // 🚨 테스트가 매번 동일한 조건에서 실행되도록 초기 재고를 설정합니다.
        // stockService.updateStock(TEST_PROD_NO, INITIAL_STOCK_QTY);
        successCount.set(0);
        failCount.set(0);
        logger.info("===== 테스트 환경 설정 완료: 초기 재고 {}개 =====", INITIAL_STOCK_QTY);
    }

    @Test
    @DisplayName("동시에 여러 주문 요청 시 재고 차감 및 동시성 검증")
        // @WithMockUser 제거!
    void concurrentOrderTest() throws InterruptedException {
        Integer requiredStock = 1;

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        logger.info("=== 동시성 검증 시작: {}개의 스레드가 동시에 주문 요청 ===", THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            final int userIdIndex = i;
            executorService.submit(() -> {
                try {
                    // 1. 각 스레드에 대한 SecurityContext 수동 생성 및 설정
                    String userId = "user" + userIdIndex;
                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userId, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
                    );
                    context.setAuthentication(authentication);
                    SecurityContextHolder.setContext(context);

                    // 2. 주문 로직 실행
                    MockHttpServletRequest mockRequest = new MockHttpServletRequest();
                    Integer price = productService.selectNowOrdProduct(TEST_PROD_NO).getPrice();
                    OrderDto orderDto = OrderDto.builder().orderDate(LocalDate.now()).totalAmount(requiredStock).userId(userId).build();
                    List<OrderDetailDto> orderDetails = List.of(OrderDetailDto.builder().prodNo(TEST_PROD_NO).qty(requiredStock).price(price).build());

                    //String orderNo = orderService.createOrder(orderDto, orderDetails, null, mockRequest);
                    String orderNo = orderService.createOrder(orderDto, orderDetails, null, userId);

                    successCount.incrementAndGet();
                    // logger.info("주문 성공: user{}, 주문번호: {}", userIdIndex, orderNo);

                } catch (BusinessException e) {
                    failCount.incrementAndGet();
                    logger.warn("주문 실패 (재고 부족 등): user{} - {}", userIdIndex, e.getMessage());
                } catch (Exception e) {
                    failCount.incrementAndGet();
                    logger.error("치명적인 기타 예외: user{} - {}", userIdIndex, e.getMessage(), e);
                } finally {
                    // 3. 스레드 로컬 정리 (매우 중요)
                    SecurityContextHolder.clearContext();
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        logger.info("=== 동시성 검증 완료 ===");
        logger.info("총 시도: {}, 성공: {}, 실패: {}", THREAD_COUNT, successCount.get(), failCount.get());

        // 4. 최종 결과 검증 (DB 조회)
        // long finalStock = stockService.getCurrentStock(TEST_PROD_NO);
        // long expectedStock = INITIAL_STOCK_QTY - successCount.get();
        // Assertions.assertEquals(expectedStock, finalStock);
        // logger.info("최종 재고: {}, 예상 재고: {}", finalStock, expectedStock);
    }
}