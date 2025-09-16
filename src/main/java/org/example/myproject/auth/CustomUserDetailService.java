package org.example.myproject.auth;

import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.Logger;



@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private AuthMapper authMapper;

    private static final Logger logger = LogManager.getLogger(CustomUserDetailService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("username : {}", username);
        AuthDTO authDTO = authMapper.selectUserInfo(username);

        logger.info("current userDTO : {}", authDTO);

        if (authDTO == null) {
            throw new UsernameNotFoundException("user not found with username" + username);
        }

        return authDTO;
    }

}
