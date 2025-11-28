package org.example.myproject.order.service;

import jakarta.annotation.Nullable;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.mapper.AuthMapper;
import org.example.myproject.auth.service.AuthService;
import org.example.myproject.cart.dto.CartDto;
import org.example.myproject.cart.mapper.CartMapper;
import org.example.myproject.error.BusinessException;
import org.example.myproject.error.ErrorCode;
import org.example.myproject.library.service.LibraryService;
import org.example.myproject.order.dto.*;
import org.example.myproject.order.mapper.OrderMapper;
import org.example.myproject.order.mapper.OrderSequenceMapper;
import lombok.RequiredArgsConstructor;
import org.example.myproject.product.dto.ProductCommonDto;
import org.example.myproject.product.dto.ProductDto;
import org.example.myproject.product.dto.ProductImageDto;
import org.example.myproject.product.mapper.ProductMapper;
import org.example.myproject.product.service.ProductService;
import org.example.myproject.stock.dto.StockQtyDto;
import org.example.myproject.stock.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class OrderService {

    @Autowired
    OrderSequenceMapper orderSequenceMapper;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    OrderNumberGeneratorService orderNumberGeneratorService;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    AuthService authService;

    @Autowired
    LibraryService libraryService;

    @Autowired
    StockService stockService;

    @Autowired
    ProductService productService;

    private static final Logger logger = LogManager.getLogger(OrderService.class);


    // 1. createOrder 설계 미스. 즉시 주문, 카트 주문 동시 사용하게 설계했으나 단일 주문은 단독 주문이라는 걸 고려하지
    // 못하여 파라미터를 List.of로 굳이 바꿔서 createOrder 보내는 수고를 해야 함. 이에 대해서 차후 리펙토링 필요하다고 생각.
    // 2. 지금 보니 orderMaster 파라미터는 불필요하다. 장바구니가 안정화 되면, 제거하는 방향으로 리펙토링.
    // 3. 결제 도입 시엔 현재 메소드는 먼저 결제 후 주문하는 preCreateOrder로 리펙토링하고, conform 메소드를 따로 만들어 처리하는 방법으로 고민. 선 결제 시에도 OrderCrate 후 상태값을 남김. -> 결제 완료 상태일 경우 conform에서 바로 처리.
    @Transactional
//    public String createOrder(OrderDto orderMaster, List<OrderDetailDto> orderDetails) {
    //public String createOrder(OrderDto orderMaster, List<OrderDetailDto> orderDetails, @Nullable List<CartDto> cartDto, HttpServletRequest request) {
    public String createOrder(OrderDto orderMaster, List<OrderDetailDto> orderDetails, @Nullable List<CartDto> cartDto, String userId) {

        Map<Long, Integer> requestQuantities = orderDetails.stream()
                .collect(Collectors.toMap(
                        OrderDetailDto::getProdNo,
                        OrderDetailDto::getQty
                ));

// DB 조회에 필요한 상품 번호 리스트
        //List<Long> prodNos = new ArrayList<>(requestQuantities.keySet());
        List<StockQtyDto> stock = stockService.selectStockQty(requestQuantities);

        // 2025/11/13 일단 추가.
        Map<Long, StockQtyDto> stockMap = stock.stream()
                .collect(Collectors.toMap(StockQtyDto::getProdNo, dto -> dto));

        orderDetails.forEach(details -> {
            Long prodNo = details.getProdNo();
            StockQtyDto stockData = stockMap.get(prodNo);

            if (stockData != null) {
                details.setProdType(stockData.getProdType());
            }
        });

        //List<StockQtyDto> stock = stockService.selectStockQty(prodNos, requestQty);

        List<String> notEnoughProdNames = stock.stream()
                .filter(dto -> {

                    Integer requestedQty = requestQuantities.get(dto.getProdNo());

                    return dto.getStockQty() < requestedQty;
                })
                .map(StockQtyDto::getProdName)
                .toList();



        if (!notEnoughProdNames.isEmpty()) {

            String productNames = String.join(", ", notEnoughProdNames);

            throw new BusinessException(ErrorCode.STOCK_NOT_ENOUGH_DETAIL, productNames);
        }

        if (orderMaster == null) {
//            throw new IllegalArgumentException("주문 상품이 비어 있습니다.");
            throw new BusinessException(ErrorCode.PRODUCT_ORDER_NOT_FOUND);
        }

        String orderNo = orderNumberGeneratorService.generateOrderNumber();

        // 주문 처리 여기에 추가할 것. insertOrderMaster(OrderDto order);
        // 주문 처리 여기에 추가할 것. insertOrderDetail(OrderDto orderDetail);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

//        String userId = authService.getAuthenticUserId(request);

//        if (authentication == null || !authentication.isAuthenticated() || !(authentication.getPrincipal() instanceof UserDetails)) {
//            logger.info("authentication: " + authentication);
//            logger.info("!authentication.isAuthenticated() : " + !authentication.isAuthenticated());
//            logger.info("!(authentication instanceof UserDetails) " + !(authentication instanceof UserDetails));
//
//            //throw new RuntimeException("로그인 필요");
//            throw new BusinessException(ErrorCode.REQUEST_LOGIN);
//        }
//
//
//
//
//        Object principal = authentication.getPrincipal();
//        if (principal instanceof UserDetails) {
//            userId = ((UserDetails)authentication.getPrincipal()).getUsername();
//            logger.info("userId :" + userId);
//        } else {
//            throw new RuntimeException("사용자 정보 확인 불가.");
//        }

        orderMaster.setUserId(userId);
        orderMaster.setOrderNo(orderNo);
//        logger.info("orderMaster :" + orderMaster);

        // 주문 번호 존재 여부 체크에 따른 비즈니스 예외 추가 검토.
        orderMapper.insertOrderMaster(orderMaster);


//        for (OrderDetailDto details : orderDetails) {
//            details.setOrderNo(orderNo);
//            orderMapper.insertOrderDetail(details);
//            stockService.decreaseStock(details.getProdNo(), details.getQty());
//        }

        orderDetails.forEach((details -> details.setOrderNo(orderNo)));


        orderMapper.insertOrderDetailBulk(orderDetails);

        Integer updatedStock = stockService.decreaseStockBulk(orderDetails); // for문 안 쓰고 좀 더 효율적으로 일괄 처리하도록 리팩토링 함. 2025/11/04

        if(updatedStock < orderDetails.size()) {
            throw new BusinessException(ErrorCode.STOCK_NOT_ENOUGH_CONCURRENCY);
        }

        /*=== 통합 테스트용 ===*/
        List<StockQtyDto> stockQtyDtos = stockService.selectStockQty(requestQuantities);

        logger.info("차감 후 재고 : " + stockQtyDtos.get(0).getProdName() + ", 남은 수량 : " + stockQtyDtos.get(0).getStockQty());
        /*========================*/


//        for (CartDto cartNos : cartDto) {
//            Long cartNo = cartNos.getCartNo();
//            cartMapper.deleteCart(cartNo);
//        }
        if (cartDto != null && !cartDto.isEmpty()) {
            List<Long> cartNos = cartDto.stream().map(CartDto::getCartNo).toList();

            cartMapper.deleteCartBulk(cartNos);
        }

        List<OrderCompleteDto> orderCompleteDtos = orderDetails.stream()
                .map(detail -> OrderCompleteDto.builder()
                .prodNo(detail.getProdNo())
                .qty(detail.getQty())
                        .build()).toList();

        this.completeOrder(orderCompleteDtos, userId);

        // 주문 내 아이템 개수에 따라 라이브러리 추가도 복수가 되어야 함을 깜박함..


        return orderNo;
    }


    //public List<OrderListDTO> orderList(int pageSize, int offset) {
    public List<OrderListDTO> orderList(int pageSize, int offset, String userId) {
        //return orderMapper.orderList(pageSize, offset);
        return orderMapper.orderList(pageSize, offset, userId);
    }

    public int selectOrdListCount(String userId) {
        return orderMapper.selectOrdListCount(userId);
    };

    // 주문번호 조회
    public String selectOrderNum(String orderNo) {
        return orderMapper.selectOrderNum(orderNo);
    }

    public OrderInfoDto selectOrderInfo(String orderNo, String userId) {
        return orderMapper.selectOrderInfo(orderNo, userId);
    }

    // 2025/11/12 추가
    // 최적화를 위해 주문 결과에 보이는 OrderNo에 따른
    // prodNos와 prodNos에 따른 Image들을 각각 불러와서 service 조립 연습.
   public OrderInfoDto orderDetails(String orderNo, String userId) {
        OrderInfoDto orderInfo = selectOrderInfo(orderNo, userId);


        if (orderInfo == null || orderInfo.getOrdInfoProdList() == null || orderInfo.getOrdInfoProdList().isEmpty()) {
            return orderInfo;
        }

        List<Long> prodNos = orderInfo.getOrdInfoProdList().stream()
                .map(OrderInfoProductDto::getProdNo)
                .distinct()
                .toList();


        List<ProductImageDto> allImages = productService.selectImagesByProdNos(prodNos);


        Map<Long, List<ProductImageDto>> imageMap = allImages.stream()
                .collect(Collectors.groupingBy(ProductImageDto::getProdNo));

        orderInfo.getOrdInfoProdList().forEach(product -> {
            List<ProductImageDto> imageList = imageMap.get(product.getProdNo());
            product.setImageList(imageList = imageMap.get(product.getProdNo()));
        });

        return orderInfo;



    };

    @Transactional
    public void completeOrder(List<OrderCompleteDto> orderCompleteDto, String userId) {

        // 재사용성 생각해서 귀찮아도 그냥 파라미터 따로 제외해서 하자.
        List<Long> prodNos = orderCompleteDto.stream().map(OrderCompleteDto::getProdNo).toList();

        int result1 = libraryService.insertLibraryItems(prodNos, userId);

        // 재고 차감과 판매수 업데이트 각각 용도에 따른 명확한 역할 분리냐, 아니면 한 번
        // 호출로 여러 테이블을 업데이트하여 부하를 줄이느냐 중 고민..


        if(prodNos.size() > result1) {
            throw new BusinessException(ErrorCode.FAIL_ADD_LIBRARY);
        }

        List<ProductCommonDto.QtyUpdate> qtyUpdate = orderCompleteDto.stream()
                .map(item -> ProductCommonDto.QtyUpdate.builder()
                        .prodNo(item.getProdNo())
                        .qty(item.getQty())
                        .build())
                .toList();

        int result2 = productService.updateSalesCount(qtyUpdate);

        if(prodNos.size() > result2) {
            throw new BusinessException(ErrorCode.FAIL_ADD_LIBRARY);
        }

    }

}
