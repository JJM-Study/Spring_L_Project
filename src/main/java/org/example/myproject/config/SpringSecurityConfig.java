package org.example.myproject.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.user.UserController;
import org.example.myproject.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

    private static final Logger logger = LogManager.getLogger(SpringSecurityConfig.class);

    //https://docs.spring.io/spring-security/reference/servlet/appendix/namespace/http.html#nsa-remember-me-attributes , 참고

    @Bean
    public SecurityFilterChain securityException(HttpSecurity http) throws Exception {

        /* 로그인 */
        http.authorizeHttpRequests((requests) -> requests
                .requestMatchers("/login").permitAll()
                .anyRequest().permitAll()
        );


        // .usernameParameter, .usernameParameter(), loginProcessingUrl는 username, password, login 기본값으로 하겠음.
        /* 로그인 Form */
        http.formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/home", true)
                .permitAll()
                .successHandler((request, response, authentication) -> {
                            logger.info("Login Success : {}", authentication.getPrincipal());
                        })
                .failureHandler(((request, response, exception) -> {
                            logger.error("Login Failed : exception : {}, request : {}, response : {}", exception.getMessage(), request.getParameter("username"), response.getStatus());
                }))
        );

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

//
//    @Bean
//    public PasswordEncoder passwordEncoder() {}
//            PasswordEncoderFactories.createDelegatingPasswordEncoder();

}
