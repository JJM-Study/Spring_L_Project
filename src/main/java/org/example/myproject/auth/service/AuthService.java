package org.example.myproject.auth.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.myproject.auth.dto.AuthDTO;
import org.example.myproject.auth.mapper.AuthMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public String existId(String username) {

        return authMapper.existId(username);
    }
}
