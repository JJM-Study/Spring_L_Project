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

    public List<ProductDto> selectProductList(int pageSize, int offset, String title) {

        return productMapper.selectProductList(pageSize, offset, title);
    }

    public ProductDetailDto selectProductDetail(String prodNo) {

        return productMapper.selectProductDetail(prodNo);
    }

    public List<ProductPriceDto> selectProductPrice(List<Long> prodNo) {

        return productMapper.selectProductPrice(prodNo);
    }

    //public int selectProductCount() {
    public int selectProductCount(String title) {

        return productMapper.selectProductCount(title);
    }

    public ProductDto selectNowOrdProduct(Long prodNo) {
        return productMapper.selectNowOrdProduct(prodNo);
    }

}
