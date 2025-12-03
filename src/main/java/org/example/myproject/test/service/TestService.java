package org.example.myproject.test.service;

import org.example.myproject.product.dto.ProductDetailDto;
import org.example.myproject.product.mapper.ProductMapper;
import org.example.myproject.test.mapper.TestMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private TestMapper testMapper;

    public TestService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    public void insertProdDetail(ProductDetailDto productDetailDto) {
        testMapper.insertProdDetail(productDetailDto);
    }
}
