package org.example.myproject.auth.dto;

import lombok.Data;

@Data
public class SignUpDTO {
    private String username;
    private String password;
    private String nickname;
    private String email;
    private String checkPassword;

}
