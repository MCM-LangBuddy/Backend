package at.heapheaparray.langbuddy.server.controller;

import at.heapheaparray.langbuddy.server.dao.models.User;
import at.heapheaparray.langbuddy.server.dao.repositories.UserRepository;
import at.heapheaparray.langbuddy.server.dto.response.AuthSuccess;
import at.heapheaparray.langbuddy.server.dto.requests.LoginRequest;
import at.heapheaparray.langbuddy.server.dto.requests.RegisterRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.crypto.password.PasswordEncoder;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public AuthSuccess register(@RequestBody RegisterRequest data) {
        User existing = userRepository.findByUserName(data.getUserName());
        if(existing!=null) {
            return new AuthSuccess("Username exists!");
        }
        User newUser = User.builder()
                .userName(data.getUserName())
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .password(passwordEncoder.encode(data.getPassword()))
                .build();

        userRepository.save(newUser);

        return new AuthSuccess("Success", newUser.getId());
    }

    @PostMapping("/login")
    public AuthSuccess login(@RequestBody LoginRequest data) {
        User testUser = userRepository.findByUserName(data.getUserName());
        if(testUser!=null && passwordEncoder.matches(data.getPassword(), testUser.getPassword())) {
            return new AuthSuccess("Success", testUser.getId());
        }
        return new AuthSuccess("Login failed!");
    }
}
