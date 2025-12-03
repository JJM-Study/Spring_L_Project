package org.example.myproject.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;



// : https://juu-code.tistory.com/48 관련 개념 정리
// https://docs.spring.io/spring-security/reference/6.5-SNAPSHOT/servlet/architecture.html#servlet-exceptiontranslationfilter



public class XssFilter implements Filter {

    private final Safelist safelist;


    // 모든 웹 애플리케이션의 요청/응답을 가로채고 처리하는 모든 서블릿 필터의 기본 골격이라고 볼 수 있다.

    public XssFilter(Safelist safelist) {
        this.safelist = safelist;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        // doFilter 메서드 내부에서는 인자가 ServletRequest로 선언되어 있기 때문에 .

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
       //XssReReadableRequestWrapper wrappedRequest = new XssReReadableRequestWrapper(httpServletRequest);
        XssReReadableRequestWrapper wrappedRequest = new XssReReadableRequestWrapper(httpServletRequest, this.safelist);


        chain.doFilter(wrappedRequest, response);
    }

    @Override
    public void init(jakarta.servlet.FilterConfig filterConfig) throws jakarta.servlet.ServletException {

    }

    @Override
    public void destroy() {

    }

}