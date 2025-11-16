package org.example.myproject.product.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final Logger logger = LogManager.getLogger(ProductController.class);




    // 파일 업로드의 설계 방식을 고민 중..
    // /app/product/도메인/년/월/일/파일
    @GetMapping("/{fileNo}/download")
    public ResponseEntity<> downloadFile(@PathVariable Long fileNo, HttpServletRequest request) {

        String filePath = "";

        String userId = authService.getAuthenticUserId(request);

        return ;
    }
}
