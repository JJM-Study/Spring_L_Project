package org.example.myproject.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @GetMapping("/errorrrr")
    public void testError() {
        throw new RuntimeException("의도적으로 발생시킨 에러");
    }
}
