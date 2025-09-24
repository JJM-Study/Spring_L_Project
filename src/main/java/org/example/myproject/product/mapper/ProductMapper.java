package org.example.myproject.product.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.myproject.product.dto.ProductDetailDto;
import org.example.myproject.product.dto.ProductDto;
import org.example.myproject.product.dto.ProductPriceDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<ProductDto> selectProductList(int pageSize, int offset);

    ProductDetailDto selectProductDetail(String prodNo);

    List<ProductPriceDto> selectProductPrice(@Param("prodNos") List<Long> prodNo);

    int selectProductCount();
}
