package org.example.myproject.auth.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.myproject.auth.dto.AuthDTO;

@Mapper
public interface AuthMapper {
    AuthDTO selectUserInfo(String username);

    boolean signUpUser(AuthDTO authDTO);

    String existId(String username);
}
