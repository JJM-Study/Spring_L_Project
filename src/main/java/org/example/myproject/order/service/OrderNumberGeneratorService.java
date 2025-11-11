package org.example.myproject.order.service;

import org.apache.catalina.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.example.myproject.cart.service.CartService;
import org.example.myproject.order.mapper.OrderSequenceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class OrderNumberGeneratorService {

    private static final Logger logger = LogManager.getLogger(OrderNumberGeneratorService.class);

    @Autowired
    OrderSequenceMapper orderSequenceMapper;


    @Transactional
    public String generateOrderNumber() {
        //logger.info("generateOrderNumber");

        try {

            LocalDate today = LocalDate.now();
            Date seqDate = java.sql.Date.valueOf(today);

            Integer currentSeq = orderSequenceMapper.selectForUpdate(seqDate);

            if (currentSeq == null) {
                orderSequenceMapper.insertSequence(seqDate, 1);
                currentSeq = 1;
            } else {
                orderSequenceMapper.updateSequence(seqDate);
                currentSeq += 1;
            }

            // 주문번호 : ORD20250606-0001 (ORD + 날짜 + 시퀀스 / MES 당시 사용하던 방식.)
            String formattedSeq = String.format("%04d", currentSeq);

            return "ORD" + today.format(DateTimeFormatter.BASIC_ISO_DATE) + "-" + formattedSeq;

        } catch(Exception e) {
            System.out.println("Failed To Make Order No : " + e.getMessage());
            throw new RuntimeException(e);
        }

    }

}
