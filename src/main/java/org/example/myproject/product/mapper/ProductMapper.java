package org.example.myproject.product.mapper;

import org.apache.ibatis.annotations.Param;
import org.example.myproject.product.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {
    //List<ProductDto> selectProductList(int pageSize, int offset, String title);
    List<ProductDto> selectProductList(int pageSize, int offset, String title, String userId);

    ProductDetailDto selectProductDetail(String prodNo, String userId);

    List<ProductPriceDto> selectProductPrice(@Param("prodNos") List<Long> prodNos);

    int selectProductCount(@Param("title") String title);

    ProductDto selectNowOrdProduct(@Param("prodNo") Long prodNo);


    List<ProductImageDto> selectImagesByProdNos(@Param("list") List<Long> prodNo);


    List<ProductDisplayListDto> displayProductList(String userId, String listType);

    int updateSalesCount(@Param("list") List<ProductCommonDto.QtyUpdate> qtyUpdates);
}
