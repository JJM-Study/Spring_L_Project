package org.example.myproject.product.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.myproject.product.dto.ProductCheckFileDto;
import org.example.myproject.product.dto.ProductPriceDto;

@Mapper
public interface ProductFileMapper {

    ProductCheckFileDto checkDownloadFile(Long prodNo, String userId);


}
