package kg.peaksoft.peaksoftlmsb6.controller;

import kg.peaksoft.peaksoftlmsb6.dto.Login;
import kg.peaksoft.peaksoftlmsb6.dto.LoginResponse;
import kg.peaksoft.peaksoftlmsb6.dto.UserRequest;
import kg.peaksoft.peaksoftlmsb6.dto.UserResponse;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.security.jwt.JwtTokenUtil;
import kg.peaksoft.peaksoftlmsb6.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
@RequestMapping("api/jwt")
public class AuthController {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final Login login;
    private final AuthenticationManager authenticationManager;

    @PostMapping("login")
    public ResponseEntity<LoginResponse> getLogin(@RequestBody UserRequest request){
        try{
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(request.getEmail(),
                    request.getPassword());
            authenticationManager.authenticate(token);
            User user = userRepository.findByEmail(token.getName()).get();
            return ResponseEntity.ok().body(login.toLoginView(jwtTokenUtil.generateToken(user),"Successfully",user));
        }catch (BadCredentialsException ex){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(login.toLoginView("","login failed", null));
        }

    }
}
