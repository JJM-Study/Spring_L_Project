package org.example.myproject.config;

import org.jsoup.safety.Safelist;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${file.upload-dir}")
    private String uploadDir;

    @Value("${file.resource-path}")
    private String resourcePath;


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //Path path = Paths.get(uploadDir);

        // 이식성, 유연성, 안정성.
        String pathUri = Paths.get(uploadDir).toAbsolutePath().toUri().toString();


        registry.addResourceHandler(resourcePath)
                .addResourceLocations(pathUri);
    }

    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterFilterRegistrationBean() {
        FilterRegistrationBean<XssFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new XssFilter());
        registrationBean.addUrlPatterns("/product/detail/*");
        registrationBean.setOrder(2);
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AnonymousIdFilter> anonymousIdFilter() {
        FilterRegistrationBean<AnonymousIdFilter> registrationBean = new FilterRegistrationBean<>();
        registrationBean.setFilter(new AnonymousIdFilter());
        registrationBean.addUrlPatterns("/*");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public Safelist safelist() {
        return Safelist.basicWithImages()
                // .addAttributes("태그명", "속성명")
                .addAttributes("img", "style", "class", "width", "height")
                .addAttributes("p", "style", "class")
                .addAttributes("span", "style", "class")
                .addAttributes("div", "style", "class")
                .addTags("h1", "h2", "h3", "h4", "h5");
    }

}
