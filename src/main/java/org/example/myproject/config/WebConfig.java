package org.example.myproject.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {


    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterFilterRegistrationBean() {
        FilterRegistrationBean<XssFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new XssFilter());
        registrationBean.addUrlPatterns("/product/detail/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }
}
