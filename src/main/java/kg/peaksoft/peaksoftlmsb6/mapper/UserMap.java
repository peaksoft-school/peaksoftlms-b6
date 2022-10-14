package kg.peaksoft.peaksoftlmsb6.mapper;

import kg.peaksoft.peaksoftlmsb6.dto.UserRequest;
import kg.peaksoft.peaksoftlmsb6.dto.UserResponse;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class UserMap {

    public User mapToEntity (UserRequest request){
        return User.builder()
                .email(request.getEmail())
                .isBlock(false)
                .role(request.getRole())
                .build();
    }

    public UserResponse mapToResponse(User user){
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .isBlock(user.getIsBlock())
                .role(user.getRole())
                .build();
    }

}
