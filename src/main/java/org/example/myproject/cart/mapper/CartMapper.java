package org.example.myproject.cart.mapper;

import org.example.myproject.cart.dto.CartDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CartMapper {
    List<CartDto> selectOrderCartList(@Param("orderColumn") String orderColumn, @Param("orderType") String orderType);

    int addToOrderCart(CartDto cartDto);

    void deleteCart(@Param("cartNo") Long cartNo);

    List<CartDto> selectOrderCartItemsById(@Param("cartNos") List<Long> cartNos);
}
