package org.example.myproject.test.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.myproject.product.dto.ProductDetailDto;
import org.example.myproject.test.service.TestService;

@Mapper
public interface TestMapper {
    void insertProdDetail(ProductDetailDto productDetailDto);
}
