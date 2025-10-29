package org.example.myproject.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.dto.AuthDTO;
import org.example.myproject.auth.mapper.AuthMapper;
import org.example.myproject.error.BusinessException;
import org.example.myproject.error.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Service
public class AuthService {
    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LogManager.getLogger(AuthService.class);


    @Transactional
    public boolean signUpUser(AuthDTO authDTO) {

        authDTO.setPassword(passwordEncoder.encode(authDTO.getPassword()));
        return authMapper.signUpUser(authDTO);
    }

    // userId 모듈화
    public String getAuthenticUserId(HttpServletRequest request) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String userId;

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {

            Object getIdattribute = request.getAttribute("anonymous_user_id");

            return getIdattribute.toString();

        } else if (authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userDetails.getUsername();
        } else {
            throw new BusinessException(ErrorCode.AUTHENTICATION_ERROR);
        }





    }

    public String existId(String username) {

        return authMapper.existId(username);
    }
}
