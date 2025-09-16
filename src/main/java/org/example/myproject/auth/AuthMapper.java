package org.example.myproject.auth;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthMapper {
    AuthDTO selectUserInfo(String userId);

    int signUpUser(AuthDTO authDTO);
}
