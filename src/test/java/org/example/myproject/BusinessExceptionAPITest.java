package org.example.myproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.myproject.auth.service.AuthService;
import org.example.myproject.error.BusinessException;
import org.example.myproject.error.ErrorCode;
import org.example.myproject.order.dto.OrderRequestDto;
import org.example.myproject.product.dto.ProductDto;
import org.example.myproject.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.example.myproject.order.service.OrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class BusinessExceptionAPITest {

    private static Logger logger = LoggerFactory.getLogger(BusinessExceptionAPITest.class);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("주문 실패 시 예외 검증")
    @WithMockUser("MockTester")
    void orderExceptionJsonTest() throws Exception {

        ProductDto productDto = new ProductDto();
        productDto.setPrice(10000);
        given(productService.selectNowOrdProduct(any())).willReturn(productDto);

        given(authService.getAuthenticUserId(any())).willReturn("MockTester");

        doThrow(new BusinessException(ErrorCode.STOCK_NOT_ENOUGH))
                .when(orderService)
                .createOrder(any(), any(), any(), any());

        Map<String, Object> simpleBody = new HashMap<>();
        OrderRequestDto orderRequestDto = new OrderRequestDto();
        orderRequestDto.setProdNo(1L);
        orderRequestDto.setQty(2);
        orderRequestDto.setTotalPrice(20000);


        mockMvc.perform(post("/order/order_prod")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andDo(print())

                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value("S001"))
                .andExpect(jsonPath("$.message").value("상품의 재고가 없습니다."));

        }
    }
