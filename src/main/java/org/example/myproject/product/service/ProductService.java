package org.example.myproject.product.service;

import org.example.myproject.product.dto.*;
import org.example.myproject.product.mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductMapper productMapper;

    //public List<ProductDto> selectProductList(int pageSize, int offset, String title) {
    public List<ProductDto> selectProductList(int pageSize, int offset, String title, String userId) {

        //return productMapper.selectProductList(pageSize, offset, title);
        return productMapper.selectProductList(pageSize, offset, title, userId);
    }

    public ProductDetailDto selectProductDetail(String prodNo, String userId) {

        return productMapper.selectProductDetail(prodNo, userId);
    }

    public List<ProductPriceDto> selectProductPrice(List<Long> prodNo) {

        return productMapper.selectProductPrice(prodNo);
    }

    //public int selectProductCount() {
    public int selectProductCount(String title) {

        return productMapper.selectProductCount(title);
    }

    // 2025/10/25 추가. main, sub 이미지 분류 위함.
    public SendImageDTO isMainImage(ProductDetailDto detailDto) {

        SendImageDTO sendImageDTO = new SendImageDTO();
        List<ProductImageDto> subImages = new ArrayList<>();


        for(ProductImageDto images : detailDto.getImageList()) {
            if (images.getIsMain()) {
                sendImageDTO.setMainImage(images);
            } else {
                subImages.add(images);
            }
        }

        sendImageDTO.setSubImages(subImages);

        return sendImageDTO;
    }

    public ProductDto selectNowOrdProduct(Long prodNo) {
        return productMapper.selectNowOrdProduct(prodNo);
    }

    public List<ProductImageDto> selectImagesByProdNos(List<Long> prodNo) {
        return productMapper.selectImagesByProdNos(prodNo);
    }

}
