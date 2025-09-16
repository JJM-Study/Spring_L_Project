package org.example.myproject.auth;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private AuthMapper authMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Logger logger = LogManager.getLogger(AuthService.class);


    public int signUpUser(AuthDTO authDTO) {

        authDTO.setUserPw(passwordEncoder.encode(authDTO.getUserPw()));
        return authMapper.signUpUser(authDTO);
    }

}
