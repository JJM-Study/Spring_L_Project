package org.example.myproject;

import org.example.myproject.cart.dto.CartDto;
import org.example.myproject.error.BusinessException;
import org.example.myproject.library.mapper.LibraryMapper;
import org.example.myproject.order.dto.OrderDetailDto;
import org.example.myproject.order.dto.OrderDto;
import org.example.myproject.order.dto.OrderInfoDto;
import org.example.myproject.order.dto.OrderListDTO;
import org.example.myproject.order.mapper.OrderMapper;
import org.example.myproject.order.service.OrderService;
import org.example.myproject.product.service.ProductService;
import org.example.myproject.stock.dto.StockQtyDto;
import org.example.myproject.stock.service.StockService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Commit;
import org.springframework.test.annotation.DirtiesContext;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootTest
public class OrderServiceConcurrencyTest {

    @Autowired
    OrderService orderService;

    @Autowired
    StockService stockService;

    @Autowired
    ProductService productService;

    @Autowired
    LibraryMapper libraryMapper;

    @Autowired
    OrderMapper orderMapper;

    private static Logger logger = LoggerFactory.getLogger(OrderServiceConcurrencyTest.class);

    //private final int THREAD_COUNT = 100;
    private final int THREAD_COUNT = 10;
    private final Long TEST_PROD_NO = 100L; // 테스트할 상품 번호.
    //private final Integer INITIAL_STOCK_QTY = 1200; // 초기 재고 설정용 // updateStock 추가 고려
    private final Integer INITIAL_STOCK_QTY = 5; // 초기 재고 설정용 // updateStock 추가 고려

    @Test
    @WithMockUser("ConcurrencyTester")
    @DisplayName("동시에 여러 주문 요청 시 재고 차감 및 동시성 검증")
    void concurrentOrderTest() throws InterruptedException {
        Integer requiredStock = 1;
        String user = "user";

        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(THREAD_COUNT);

        StockQtyDto stockQtyDto = StockQtyDto.builder().stockQty(INITIAL_STOCK_QTY).prodNo(TEST_PROD_NO).build();
        stockService.updateProdStock(stockQtyDto);

        logger.info("=== Thread 통한 동시성 검증 시작 {} 명 동시 주문. ===", THREAD_COUNT);

        List<StockQtyDto> checkStock = stockService.selectStockQty(Map.of(TEST_PROD_NO, requiredStock));
        logger.info("주문 실행 전 남은 재고 :\n=== 상품명 : {}, 재고 : {} ===" , checkStock.get(0).getProdName(), checkStock.get(0).getStockQty());


            List<String> users = IntStream.range(0, THREAD_COUNT)
                            .mapToObj(i -> user + i)
                                    .toList();


        libraryMapper.deleteLibraries(users);

        for (int i = 0 ; i < THREAD_COUNT; i++) {
            final int userIdIndex = i;
            executorService.submit(() -> {
                try {
                    //MockHttpServletRequest mockRequest = new MockHttpServletRequest();
                    //mockRequest.setUserPrincipal(() -> "user" + userIdIndex);
                    String userId = user + userIdIndex;

                    //String userId = mockRequest.getUserPrincipal().getName();
                    Integer price = productService.selectNowOrdProduct(TEST_PROD_NO).getPrice();
                    OrderDto orderDto = OrderDto.builder().orderDate(LocalDate.now()).totalAmount(requiredStock).userId(userId).orderStatus("TEST").build();
                    List<OrderDetailDto> orderDetails = List.of(OrderDetailDto.builder().prodNo(TEST_PROD_NO).qty(requiredStock).price(price).build());
                    List<CartDto> cartDto = List.of(CartDto.builder().prodNo(TEST_PROD_NO).qty(requiredStock).userId(userId).build());




                    //logger.info("재고 상황 : " + );
                    //orderService.createOrder(orderDto, orderDetails, null ,mockRequest);
                    //String list = orderService.createOrder(orderDto, orderDetails, null ,mockRequest);
                    //String list = orderService.createOrder(orderDto, orderDetails, cartDto ,mockRequest);
                    String list = orderService.createOrder(orderDto, orderDetails, cartDto , userId);
                    //logger.info("주문번호: {} ", list);




                } catch(BusinessException e) {
                    // 재고 부족 등의 BusinessException 발생 시
                    logger.warn("주문 실패 (재고 부족 등): {}", e.getMessage());
                } catch (Exception e) {
                    logger.error("기타 예외 : {}", e.getMessage());
                }
                finally {
                    latch.countDown();
                }
            });

        }


        latch.await();
        executorService.shutdown();

        logger.info("=== 동시성 검증 완료. 최종 재고 확인 필요. ===");
        checkStock = stockService.selectStockQty(Map.of(TEST_PROD_NO, requiredStock));
        logger.info("주문 실행 후 남은 재고 = 상품명 : {}, 재고 : {}" , checkStock.get(0).getProdName(), checkStock.get(0).getStockQty());


        List<OrderInfoDto> orderInfoDtos = orderMapper.selectOrders("TEST");


        libraryMapper.deleteLibraries(users);

        Integer successCount = orderInfoDtos.size();

        Integer failedCount = THREAD_COUNT - successCount;


        logger.info("총 {} 건 중 {}건 성공, {}건 롤백", THREAD_COUNT, successCount, failedCount);



        logger.info("--- 성공적으로 커밋된 주문 정보 ---");

        orderInfoDtos.forEach(order -> {
//            logger.info("OrderNo: {} | UserID: {} | TotalPrice: {}",
            logger.info("OrderNo: {} | UserID: {}",
                    order.getOrderNo(),
                    order.getUserId());
//                    order.getTotalPrice());
        });

        orderMapper.deleteOrders("TEST");

    }
}
