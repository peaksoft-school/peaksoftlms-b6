package kg.peaksoft.peaksoftlmsb6.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class LoginResponse {
    private String jwtToken;
    private String message;
    private Set<String> authorities;
}
