package org.example.myproject.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Data
@ToString
public class UserDTO implements UserDetails {
    private String userId;
    private String userPw;
    private String role;
    private String email;
    private String nickname;
    private String imageUrl;


//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return List.of(new SimpleGrantedAuthority("READ"));
//    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }


    public String getUsername() {
        return userId;
    }

    public String getPassword() {
        return userPw;
    }
}
