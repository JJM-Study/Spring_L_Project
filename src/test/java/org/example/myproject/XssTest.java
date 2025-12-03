package org.example.myproject;

import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.service.AuthService;
import org.example.myproject.product.controller.ProductController;
import org.example.myproject.product.dto.ProductDetailDto;
import org.example.myproject.product.dto.ProductDto;
import org.example.myproject.product.mapper.ProductMapper;
import org.example.myproject.test.mapper.TestMapper;
import org.example.myproject.test.service.TestService;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.logging.LoggerFactory;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.time.format.TextStyle;

import static java.util.Collections.replaceAll;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class XssTest {

    //private final Safelist safelist = Safelist.basicWithImages();

    private static final Logger logger = LogManager.getLogger(XssTest.class);

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    TestMapper testMapper;

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

    @Test
    @DisplayName("XssFilter 적용 테스트")
    @WithMockUser("tester1")
    void insertXss() throws Exception {

        String text = "<div><h1>TEST XSS</h1><script>alert('Cross Site Scripting')</script></div>";


        logger.info("=== XSS 테스트 시작 ===");

        mockMvc.perform(post("/test/detail/insert")
                .param("detailDesc", text))
                .andExpect(status().isOk());

        logger.info("Before Clean {}\n", text);

        ArgumentCaptor<ProductDetailDto> captor = ArgumentCaptor.forClass(ProductDetailDto.class);

        verify(testMapper).insertProdDetail(captor.capture());

        ProductDetailDto productDetailDto = captor.getValue();
        String cleanText = productDetailDto.getDetailDesc();

        logger.info("After Clean {}", cleanText.replaceAll( "[\r\n]+", ""));


        assertFalse(cleanText.contains("<script>"), "스크립트 남아있음.");
        assertTrue(cleanText.contains("h1"), "안전한 태그 유지.");

        logger.info("=== XSS 테스트 종료 ===");
        logger.info("============================================================================================================================================================================================================================================================");

    }


//    @Test
//    @WithMockUser("tester1")
//    @DisplayName("XssFilter 적용 증명 테스트")
//    void insertXss() throws Exception {
//
//        String maliciousText = "XSS TEST <script>alert('TEST')</script><h1>Safe Tag</h1>";
//
//        logger.info("보낸 데이터: {}", maliciousText);
//
//
//        mockMvc.perform(post("/test/detail/insert")
//                .param("detailDesc", maliciousText)
//
//        ).andExpect(status().isOk());
//
//
//        ArgumentCaptor<ProductDetailDto> captor = ArgumentCaptor.forClass(ProductDetailDto.class);
//
//        verify(testMapper).insertProdDetail(captor.capture());
//
//
//        ProductDetailDto capturedDto = captor.getValue();
//        String actualValue = capturedDto.getDetailDesc();
//
//        logger.info("매퍼까지 도달한 데이터: {}", actualValue);
//
//        assertFalse(actualValue.contains("<script>"), "스크립트 태그가 남아있음.");
//        assertTrue(actualValue.contains("<h1>"), "안전한 태그는 유지.");
//    }


}
