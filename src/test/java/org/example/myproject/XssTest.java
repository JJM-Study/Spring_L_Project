package org.example.myproject;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.service.AuthService;
import org.example.myproject.product.controller.ProductController;
import org.example.myproject.product.dto.ProductDetailDto;
import org.example.myproject.product.dto.ProductDto;
import org.example.myproject.product.mapper.ProductMapper;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class XssTest {

    //private final Safelist safelist = Safelist.basicWithImages();

    private static final Logger logger = LogManager.getLogger(XssTest.class);

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private Safelist safelist;

    @Test
    @WithMockUser("test1")
    @DisplayName("XSS 방어 테스트")
    void ProductDetailXssTest() {

        String prodNo = "2";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userId = authentication.getName();

//        String html =
//                "<h3>※ 게임 배경음악 BGM 세트 ※</h3><p>BGM 세트 상세 내용.</p><script>alert('XSS TEST')</script><img src='https://raw.githubusercontent.com/JJM-Study/Spring_L_Project/056c06b43e4674a3aff17ce1d2d4a66de8fac996/src/main/resources/static/assets/images/samples/2/Gemini_Generated_Image_2qbsev2qbsev2qbs.png' style='height:200px; width:200px;'>";

        ProductDetailDto productDetailDto = productMapper.selectProductDetail(prodNo, userId);
        String detailDesc = productDetailDto.getDetailDesc();
        String cleanDetailDesc = Jsoup.clean(detailDesc, safelist);

        logger.info("===== XSS 검증 시작 =====");

        logger.info("Before clean Data :");
        logger.info(detailDesc);


        logger.info("After clean Data :");
        //logger.info("After clean Data :\n{}", cleanDetailDesc);
        logger.info(cleanDetailDesc.replaceAll("[\r\n]+", ""));
//        logger.info(cleanDetailDesc);

        logger.info("=== XSS 테스트 종료 ===\n\n\n\n\n\n\n\n\n\n\n");

        assertFalse(cleanDetailDesc.contains("<script>"), "스크립트 태그가 남아있음.");
        assertFalse(cleanDetailDesc.contains("alert"), "자바스크립트 실행 코드가 남아있음.");

        assertTrue(cleanDetailDesc.contains("<h3>※ 게임 배경음악 BGM 세트 ※</h3>"), "안전한 태그는 유지.");
        assertTrue(cleanDetailDesc.contains("Gemini_Generated_Image_2qbsev2qbsev2qbs.png"), "이미지 태그 유지");


    }
}
