package kg.peaksoft.peaksoftlmsb6.dto;

import kg.peaksoft.peaksoftlmsb6.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserRequest {

    private String email;
    private String password;
    private Role role;


}
