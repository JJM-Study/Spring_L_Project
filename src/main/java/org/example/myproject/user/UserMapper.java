package org.example.myproject.user;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    public UserDTO selectUserInfo(String userId);
}
