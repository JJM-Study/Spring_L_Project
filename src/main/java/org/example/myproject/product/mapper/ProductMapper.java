package org.example.myproject.product.mapper;

import org.example.myproject.product.dto.ProductDetailDto;
import org.example.myproject.product.dto.ProductDto;
import org.example.myproject.product.dto.ProductPriceDto;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    List<ProductDto> selectProductList(int cPage, int offset);

    ProductDetailDto selectProductDetail(String prodNo);

    List<ProductPriceDto> selectProductPrice(List<Long> prodNo);

    int selectProductCount();
}
