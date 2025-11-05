package org.example.myproject.cart.mapper;

import org.eclipse.tags.shaded.org.apache.xpath.operations.Bool;
import org.example.myproject.cart.dto.CartDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.myproject.cart.dto.ChkCartItemDto;
import org.springframework.security.core.parameters.P;

import java.util.List;

@Mapper
public interface CartMapper {
    List<CartDto> selectOrderCartList(@Param("orderColumn") String orderColumn, @Param("orderType") String orderType, @Param("userId") String userId);

    int addToOrderCart(CartDto cartDto);

    void deleteCart(@Param("cartNo") Long cartNo);

    List<CartDto> selectOrderCartItemsById(@Param("cartNos") List<Long> cartNos);

    ChkCartItemDto chkCartItem(Long cartNo);

    boolean isInCart(@Param("prodNo") String prodNo, @Param("userId") String userId);

    // 테스트
    int selectCartCount(int cartNo);


    void deleteCartBulk(List<Long> cartNos);
}
