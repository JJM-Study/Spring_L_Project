package org.example.myproject.product.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.service.AuthService;
import org.example.myproject.file.dto.FileDownloadDto;
import org.example.myproject.product.enums.ProductFileStatus;
import org.example.myproject.product.service.ProductFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product/file")
public class ProductFileController {

    @Autowired
    AuthService authService;

    @Autowired
    ProductFileService productFileService;

    private static final Logger logger = LogManager.getLogger(ProductController.class);



    // 파일 업로드의 설계 방식을 고민 중..
    // /app/product/도메인/년/월/일/파일
    @GetMapping("/{prodNo}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long prodNo, HttpServletRequest request) {

        String userId = authService.getAuthenticUserId(request);

        // 해당 유저의 해당 아이디 번호에 대한 권한이 있나 없나
        FileDownloadDto fileDownloadDto = productFileService.getDownloadInfo(prodNo, userId);


        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileDownloadDto.getOriginalFilename() + "\"") // 파일명 설정
                .body(fileDownloadDto.getResource());
    }
}
