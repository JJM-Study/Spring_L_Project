package org.example.myproject.product.service;

import org.example.myproject.file.controller.FileController;
import org.example.myproject.file.dto.FileDownloadDto;
import org.example.myproject.file.service.FileService;
import org.example.myproject.product.dto.ProductCheckFileDto;
import org.example.myproject.product.mapper.ProductFileMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductFileService {

    private final FileService fileService;

    @Autowired
    ProductFileMapper productFileMapper;


    public ProductFileService(FileService fileService) {
        this.fileService = fileService;
    }

    public FileDownloadDto getDownloadInfo(Long prodNo, String userId) {

        ProductCheckFileDto productCheckFileDto = productFileMapper.checkDownloadFile(prodNo, userId);

        Long fileId = productCheckFileDto.getFileId();

        // 예외 처리를 어떻게 해야 할 지 고민 ...

        return fileService.loadFile(fileId);
    }


//    private void checkDownloadFile(Long prodNo, String userId) {
//
//    }


}
