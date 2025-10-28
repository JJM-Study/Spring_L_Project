package org.example.myproject.config;

import jakarta.servlet.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.text.html.Option;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class AnonymousIdFilter implements Filter {

    private static final String ANONYMOUS_ID_COOKIE_NAME = "anonymous_user_id";

    private static final int MAX_AGE_SECONDS = (int) TimeUnit.DAYS.toSeconds(30);

    private static final Logger logger = LogManager.getLogger(AnonymousIdFilter.class);


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // HTTP 프로토콜 특정화를 위한 다운 캐스팅이라고 할 수 있다.
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;


        // for 문 보단 최적화의 관점에선 stream이 나을 듯.
        Optional<Cookie> anonymousIdCookie = Arrays.stream(Optional.ofNullable(httpServletRequest.getCookies()).orElse(new Cookie[0]))
                .filter(cookie -> ANONYMOUS_ID_COOKIE_NAME.equals(cookie.getName()))
                .findFirst();

        String anonymousId;

        if (anonymousIdCookie.isPresent()) {
            anonymousId = anonymousIdCookie.get().getValue();
        } else {
            anonymousId = UUID.randomUUID().toString();

            setAnonymousIdCookie(httpServletResponse, anonymousId);

            logger.info("쿠키 " + anonymousId + "발급 완료");
        }

        request.setAttribute(ANONYMOUS_ID_COOKIE_NAME, anonymousId);

        chain.doFilter(request, response);
    }

    private void setAnonymousIdCookie(HttpServletResponse httpServletResponse, String anonymousId) {
        Cookie newCookie = new Cookie(ANONYMOUS_ID_COOKIE_NAME, anonymousId);
        newCookie.setMaxAge(MAX_AGE_SECONDS);
        newCookie.setPath("/");
        newCookie.setHttpOnly(true); // 자바스크립트로 읽기 방지.
        httpServletResponse.addCookie(newCookie);
    }

}
