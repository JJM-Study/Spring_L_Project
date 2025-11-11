package org.example.myproject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.order.dto.OrderInfoDto;
import org.example.myproject.order.mapper.OrderMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
public class TEST {

    @Autowired
    OrderMapper orderMapper;

    private static final Logger logger = LogManager.getLogger(TEST.class);

    @Test
    @WithMockUser("ANON-7004e94b-327e-4ebe-ba4a-eeffb6ea30f0")
    @DisplayName("그냥 출력 테스트")
    void outputTest() {

        List<OrderInfoDto> orderInfoDtos = orderMapper.selectOrderInfo("ANON-7004e94b-327e-4ebe-ba4a-eeffb6ea30f0", "ORD20251111-2603");
        logger.info("orderInfoDtos : {} ", orderInfoDtos);
    }
}
