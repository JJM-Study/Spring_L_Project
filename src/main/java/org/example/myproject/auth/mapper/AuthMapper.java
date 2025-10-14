package org.example.myproject.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.myproject.auth.dto.AuthDTO;
import org.springframework.security.core.parameters.P;

@Mapper
public interface AuthMapper {
    AuthDTO selectUserInfo(@Param("username") String username);

    boolean signUpUser(AuthDTO authDTO);

    String existId(@Param("username") String username);
}
