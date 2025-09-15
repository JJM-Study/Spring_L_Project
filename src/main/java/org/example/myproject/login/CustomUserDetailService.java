package org.example.myproject.login;

import org.apache.logging.log4j.LogManager;
import org.example.myproject.user.UserDTO;
import org.example.myproject.user.UserMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.Logger;



@Service
public class CustomUserDetailService implements UserDetailsService {
    private UserMapper userMapper;

    private static final Logger logger = LogManager.getLogger(CustomUserDetailService.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO userDTO = userMapper.selectUserInfo(username);

        logger.info("current userDTO : {}", userDTO);

        if (userDTO == null) {
            throw new UsernameNotFoundException("user not found with username" + username);
        }


        return userDTO;
    }
}
