package org.example.myproject.cart.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.apache.ibatis.annotations.Param;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.controller.AuthController;
import org.example.myproject.auth.service.AuthService;
import org.example.myproject.cart.dto.CartDto;
import org.example.myproject.cart.dto.ChkCartItemDto;
import org.example.myproject.cart.service.CartService;
import org.example.myproject.error.BusinessException;
import org.example.myproject.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private static final Logger logger = LogManager.getLogger(CartController.class);

    @Autowired
    private AuthService authService;

    private CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cartlist")
    public String selectOrderCartList(@RequestParam(value = "orderColumn", required = false) String orderColumn,
                                      @RequestParam(value = "orderType", required = false) String orderType,
                                      Model model,
                                      HttpServletRequest request) {


//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        Object principle = authentication.getPrincipal();
//
//        String userId = null;

//        if(principle instanceof UserDetails) {
//            userId = ((UserDetails) principle).getUsername();
//        }

        String userId = authService.getAuthenticUserId(request);

        if (orderColumn == null || orderColumn.trim().isEmpty()) {
            orderColumn = "cart_no";
        }
        if (orderType == null || orderType.trim().isEmpty()) {
            orderType = "asc";
        }

        model.addAttribute("pageTitle", "장바구니");
        model.addAttribute("orderColumn", orderColumn);
        model.addAttribute("orderType", orderType);
        model.addAttribute("layoutBody","/WEB-INF/jsp/cart/cartlist.jsp");

        List<CartDto> cartList = cartService.selectOrderCartList(orderColumn, orderType, userId);
        model.addAttribute("cartList", cartList);
//        return "cart/cartlist";
        return "layout/main-layout";
    }

    @ResponseBody
    @RequestMapping("/add")
//    public ResponseEntity<Map<String, Object>> addToOrderCart(CartDto cartDto) {
    public ResponseEntity<Map<String, Object>> addToOrderCart(@RequestBody CartDto cartDto, HttpServletRequest request) {
//    public ResponseEntity<Map<String, Object>> addToOrderCart(@RequestBody CartDto cartDto, Authentication authentication) {


//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        logger.info("cartDto.getProdNo :" + cartDto.getProdNo());
        logger.info("cartDto.getQty :" + cartDto.getQty());
//
//        Object principle = authentication.getPrincipal();
//
//        if (authentication != null) {
//            authentication.getPrincipal();
//        }
//        String userId = null;
//
//        if (principle instanceof UserDetails) {
//            userId = ((UserDetails) principle).getUsername();
//            }
//        } else {
//            throw new BusinessException(ErrorCode.REQUEST_LOGIN);
//        }
        String userId = authService.getAuthenticUserId(request);


        boolean isInCart = cartService.isInCart(cartDto.getProdNo().toString(), userId);
        if (isInCart) {
            throw new BusinessException(ErrorCode.IS_IN_CART);
        }

//        Integer qty = cartDto.getQty() == null || cartDto.getQty() <= 0 ? 1 : cartDto.getQty();
        if (cartDto.getQty() == null || cartDto.getQty() <= 0) {
            cartDto.setQty(1);
        }

        cartDto.setUserId(userId);

        int result = cartService.addToCart(cartDto);
        Map<String, Object> response = new HashMap<>();

        Boolean isSuccess = result > 0;
        response.put("success", isSuccess);
        response.put("message", isSuccess ? "장바구니에 담았습니다." : "담기 실패했습니다.");

        return ResponseEntity.ok(response);
    }


    @ResponseBody
    @PostMapping("/delete")
    public ResponseEntity<Map<String, Object>> delCart(@RequestParam("cartNo") Long cartNo, HttpServletRequest request) {
        ChkCartItemDto chkCartItemDto = cartService.chkCartItem(cartNo);

        Map<String, Object> response = new HashMap<>();
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        Object principal = authentication.getPrincipal();


        // 현재 익명 사용자 저장은 구현되어 있지 않음. cart에 세션 저장 열 추가하여 나중에 익명 사용자도 가능하게 확장할 것.

        // 전역 예외 처리 핸들러로 따로 나눌 지 고민.

//        // MAP 방식으로 response 하던 것을 DTO로 표준화 할 지 고민.
//        if ((principal instanceof UserDetails)) {
//            String userId = ((UserDetails) principal).getUsername();
//            if (!userId.equals(chkCartItemDto.getUserId())) {
//                throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS);
//            }
//        } else {
//            // 익명 사용자 처리 기능 구현 때 쯤 여기 로직 추가 필요.
//            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS);
//        }

        String userId = authService.getAuthenticUserId(request);

        if(!userId.equals(chkCartItemDto.getUserId())) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ACCESS);
        }

            response.put("success", "true");
            response.put("message", "카트가 성공적으로 삭제되었습니다.");

            cartService.deleteCart(cartNo);

            return ResponseEntity.ok(response);

    }

}
