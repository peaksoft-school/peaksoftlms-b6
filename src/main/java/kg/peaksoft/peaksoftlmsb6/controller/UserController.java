package kg.peaksoft.peaksoftlmsb6.controller;

import kg.peaksoft.peaksoftlmsb6.dto.UserRequest;
import kg.peaksoft.peaksoftlmsb6.dto.UserResponse;
import kg.peaksoft.peaksoftlmsb6.serviceImpl.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/jwt")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping("newUser")
    public UserResponse create(@RequestBody UserRequest request){
        return service.create(request);
    }
}
