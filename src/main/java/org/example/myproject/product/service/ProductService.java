package org.example.myproject.product.service;

import org.example.myproject.product.mapper.ProductMapper;
import org.example.myproject.product.dto.ProductDetailDto;
import org.example.myproject.product.dto.ProductDto;
import org.example.myproject.product.dto.ProductPriceDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductMapper productMapper;

    public List<ProductDto> selectProductList(int page, int pCount) {

        return productMapper.selectProductList(page, pCount);
    }

    public ProductDetailDto selectProductDetail(String prodNo) {

        return productMapper.selectProductDetail(prodNo);
    }

    public List<ProductPriceDto> selectProductPrice(List<Long> prodNo) {

        return productMapper.selectProductPrice(prodNo);
    }
}
