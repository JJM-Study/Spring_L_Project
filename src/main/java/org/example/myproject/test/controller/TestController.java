package org.example.myproject.test.controller;

import org.example.myproject.product.dto.ProductDetailDto;
import org.example.myproject.test.mapper.TestMapper;
import org.example.myproject.test.service.TestService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    private TestService testService;

    private TestMapper testMapper;

    TestController(TestService testService, TestMapper testMapper) {
        this.testService = testService;
        this.testMapper = testMapper;
    }

    @GetMapping("/errorrrr")
    public void testError() {
        throw new RuntimeException("의도적으로 발생시킨 에러");
    }

    @PostMapping("/test/detail/insert")
    public ResponseEntity<String> insertTest(@ModelAttribute ProductDetailDto productDetailDto) {

        testMapper.insertProdDetail(productDetailDto);

        return ResponseEntity.ok("OK");
    }
}
