package org.example.myproject.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private static final Logger logger = LogManager.getLogger(SpringSecurityConfig.class);

    @Autowired private DataSource dataSource;
    @Autowired
    private CustomUserDetailService customUserDetailService;

    //https://docs.spring.io/spring-security/reference/servlet/appendix/namespace/http.html#nsa-remember-me-attributes , 참고
    //https://docs.spring.io/spring-security/reference/servlet/appendix/namespace/http.html#nsa-saml2-logout-attributes -> 필터 체인 Attribute

    @Bean
    public SecurityFilterChain securityException(HttpSecurity http, UserDetailsService userDetailsService) throws Exception {

        /* 로그인 */
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/login", "/home", "/", "/sign-up", "/WEB-INF/**", "/product/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/sign-up").permitAll()  // POST도 명시적으로 허용

                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                .anyRequest().authenticated()
                .anyRequest().permitAll()


        );


        // .usernameParameter, .usernameParameter(), loginProcessingUrl는 username, password, login 기본값으로 하겠음.
        /* 로그인 Form */
        http.formLogin(form -> form
                .loginPage("/login")
//                .defaultSuccessUrl("/home", true)
                .permitAll()
                /* roles에 따라 다른 로그인 처리 등을 위해 커스텀 핸들러 클래스 구현 고려할 것.*/
                .successHandler((request, response, authentication) -> {
                            logger.info("Login Success : principal : {}", authentication.getPrincipal());
                            response.sendRedirect("/home");
                        })
                .failureHandler(((request, response, exception) -> {
                            logger.error("Login Failed : exception : {}, request : {}, response : {}", exception.getMessage(), request.getParameter("username"), response.getStatus());
                }))
        );

        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .deleteCookies("JSESSIONID")

        );

        // default cookie name = remember-me
        http.rememberMe(remember -> {
                    remember.rememberMeParameter("remember-me")
                            .userDetailsService(customUserDetailService)
                            .tokenValiditySeconds(1209600)
                            .tokenRepository(persistentTokenRepository());

                });

        http.csrf(csrf -> csrf.disable()); // 테스트용

        return http.build();
    }





//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource);
        return tokenRepository;
    }

//
//    @Bean
//    public PasswordEncoder passwordEncoder() {}
//            PasswordEncoderFactories.createDelegatingPasswordEncoder();

}
