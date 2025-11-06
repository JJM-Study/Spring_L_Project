package org.example.myproject;

import org.example.myproject.error.BusinessException;
import org.example.myproject.error.ErrorCode;
import org.mockito.Mock;
import org.slf4j.Logger;
import jakarta.servlet.http.HttpServletRequest;
import org.example.myproject.auth.service.AuthService;
import org.example.myproject.cart.controller.CartController;
import org.example.myproject.cart.dto.ChkCartItemDto;
import org.example.myproject.cart.mapper.CartMapper;
import org.example.myproject.cart.service.CartService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@WebMvcTest(CartController.class)
public class ControllerTest {

    private static Logger logger = LoggerFactory.getLogger(ControllerTest.class);

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CartMapper cartMapper;

    //@Autowired
    @MockBean
    AuthService authService;

//    @Autowired
@MockBean
    CartService cartService;


    @Test
    @WithMockUser("MockTester1")
    @DisplayName("장바구니 삭제 권한에 따른 예외 처리")
    void cartControllerTest() throws Exception {
        String currentUserId = "MockTester1";
        String cartUserId = "cartAccessTest";
        Long cartNo = 1L;

        logger.info("UserId Mismatching 에 따른 예외 검사 Start : ");

//        cartMapper.chkCartItem(1L);
        ChkCartItemDto cartItemDto = new ChkCartItemDto();
        cartItemDto.setUserId(cartUserId);
        when(cartService.chkCartItem(cartNo)).thenReturn(cartItemDto);

        when(authService.getAuthenticUserId(any(HttpServletRequest.class))).thenReturn(currentUserId);

        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();

        String userId;

        cartMapper.deleteCart(cartNo);


        mockMvc.perform(MockMvcRequestBuilders.post("/cart/delete")
                        .param("cartNo", String.valueOf(cartNo))
                        .with(csrf()))
                        .andExpect(status().isForbidden())
                        .andExpect(result -> assertTrue(result.getResolvedException() instanceof BusinessException))
                        .andExpect(result -> {
                                    BusinessException resolvedException = (BusinessException) result.getResolvedException();
                                    assertEquals(ErrorCode.UNAUTHORIZED_ACCESS, resolvedException.getErrorCode());
                        });

                //assertThat(cartMapper.selectOrderCartItemsById(List.of(cartNo)));
                verify(cartService, never()).deleteCart(any(Long.class));

                logger.info("verify : cartService, never implement deleteCart Success");

    }

}
