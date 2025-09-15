package org.example.myproject.login;

import org.apache.logging.log4j.LogManager;
import org.example.myproject.user.UserDTO;
import org.example.myproject.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.Logger;



@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    private UserMapper userMapper;

    private static final Logger logger = LogManager.getLogger(CustomUserDetailService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("username : {}", username);
        UserDTO userDTO = userMapper.selectUserInfo(username);

        logger.info("current userDTO : {}", userDTO);

        if (userDTO == null) {
            throw new UsernameNotFoundException("user not found with username" + username);
        }

        return userDTO;
    }

}
