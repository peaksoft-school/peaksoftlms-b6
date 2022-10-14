package kg.peaksoft.peaksoftlmsb6.serviceImpl;

import kg.peaksoft.peaksoftlmsb6.dto.UserRequest;
import kg.peaksoft.peaksoftlmsb6.dto.UserResponse;
import kg.peaksoft.peaksoftlmsb6.entity.User;
import kg.peaksoft.peaksoftlmsb6.mapper.UserMap;
import kg.peaksoft.peaksoftlmsb6.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final UserMap userMap;


    public UserResponse create(UserRequest request){
        User user = userMap.mapToEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return userMap.mapToResponse(userRepository.save(user));
    }

}
