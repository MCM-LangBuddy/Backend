package at.heapheaparray.langbuddy.server.controller;

import at.heapheaparray.langbuddy.server.dao.models.Language;
import at.heapheaparray.langbuddy.server.dao.models.User;
import at.heapheaparray.langbuddy.server.dao.repositories.UserRepository;
import at.heapheaparray.langbuddy.server.dto.response.AuthSuccess;
import at.heapheaparray.langbuddy.server.dto.requests.LoginRequest;
import at.heapheaparray.langbuddy.server.dto.requests.RegisterRequest;
import at.heapheaparray.langbuddy.server.dto.response.UserDto;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Collectors;

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
        User existing = userRepository.findByEmail(data.getEmail());
        if(existing!=null)
            throw new BadCredentialsException("User already exists!");
        }

        User newUser = User.builder()
                .email(data.getEmail())
                .firstName(data.getFirstName())
                .lastName(data.getLastName())
                .password(passwordEncoder.encode(data.getPassword()))
                .build();

        if(data.getSpokenLanguageIds() != null) {
                newUser.setSpokenLanguages(data.getSpokenLanguageIds().stream().map(Language::new).collect(Collectors.toSet()));
        }
        if(data.getLearningLanguageIds() != null) {
            newUser.setLearningLanguages(data.getLearningLanguageIds().stream().map(Language::new).collect(Collectors.toSet()));
        }

        userRepository.save(newUser);

        return new AuthSuccess("Success", newUser.getId());
    }

    @PostMapping("/login")
    public AuthSuccess login(@RequestBody LoginRequest data) {
        User testUser = userRepository.findByEmail(data.getEmail());
        if(testUser!=null && passwordEncoder.matches(data.getPassword(), testUser.getPassword())) {
            return new AuthSuccess("Success", testUser.getId());
        }
        throw new BadCredentialsException("Login Failed");
    }

    @GetMapping("/profile/{userId}")
    public UserDto getProfile(@PathVariable(name = "userId") Long userId) {
        User user = userRepository.findById(userId).orElseThrow();

        return UserDto.builder().emailAddress(user.getEmail())
                .learningLanguageDtos(user.getLearningLanguages() != null ? user.getLearningLanguages().stream().map(Language::toDto).collect(Collectors.toSet()) : null)
                .spokenLanguageDtos(user.getSpokenLanguages() != null ? user.getSpokenLanguages().stream().map(Language::toDto).collect(Collectors.toSet()) : null)
                .userId(user.getId())
                .firstName(user.getFirstName())
                .profilePictureUrl(user.getProfilePictureUrl()).build();
    }
}
