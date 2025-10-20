package org.example.myproject.config;

import org.apache.logging.log4j.LogBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

@Configuration
public class SpringSecuirtyCustomFailure {

    private static final Logger logger = LogManager.getLogger(SpringSecuirtyCustomFailure.class);

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {

        return (request, response, exception) -> {


            String errorMessage = "알 수 없는 오류입니다.";

            if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
                errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다.";
            }
            request.getSession().setAttribute("errorMessage" , errorMessage);
            logger.info("handler error : " + errorMessage);
            response.sendRedirect("/login");
        };
    }
}
