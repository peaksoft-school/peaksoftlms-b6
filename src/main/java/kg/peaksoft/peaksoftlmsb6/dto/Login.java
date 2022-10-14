package kg.peaksoft.peaksoftlmsb6.dto;

import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import org.springframework.stereotype.Component;


import java.util.HashSet;
import java.util.Set;

@Component
public class Login {

    public LoginResponse toLoginView(String token, String message, User user){
        var loginResponse = new LoginResponse();
        if (user != null){
            getAuthority(loginResponse, user.getRole());
        }
        loginResponse.setMessage(message);
        loginResponse.setJwtToken(token);
        return loginResponse;
    }

    private void getAuthority(LoginResponse loginResponse, Role role) {
        Set<String> authorities = new HashSet<>();
//        for (Role role : roles){
            authorities.add(role.toString());
//        }
        loginResponse.setAuthorities(authorities);
    }
}
