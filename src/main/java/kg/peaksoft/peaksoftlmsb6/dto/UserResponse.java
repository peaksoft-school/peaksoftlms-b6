package kg.peaksoft.peaksoftlmsb6.dto;

import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class UserResponse {

    private Long id;
    private String email;
    private Boolean isBlock = false;
    private Role role;
}
