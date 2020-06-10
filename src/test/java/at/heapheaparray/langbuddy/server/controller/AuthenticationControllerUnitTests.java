package at.heapheaparray.langbuddy.server.controller;

import at.heapheaparray.langbuddy.server.dao.models.User;
import at.heapheaparray.langbuddy.server.dao.repositories.UserRepository;
import at.heapheaparray.langbuddy.server.dto.requests.LoginRequest;
import at.heapheaparray.langbuddy.server.dto.requests.RegisterRequest;
import at.heapheaparray.langbuddy.server.dto.response.AuthSuccess;
import at.heapheaparray.langbuddy.server.dto.response.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {AuthenticationController.class, AuthenticationControllerUnitTests.PasswordEncoderMock.class})
public class AuthenticationControllerUnitTests {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationController authenticationController;
    @MockBean
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        User testUser = User.builder().id(1L)
                .firstName("Max")
                .lastName("Mustermann")
                .email("max.mustermann@gmx.net")
                .password(passwordEncoder.encode("secret123"))
                .build();

        when(userRepository.findByEmail("max.mustermann@gmx.net")).thenReturn(testUser);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(testUser));
    }

    @Test
    public void testRegisterUserSuccessful() {
        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstName("Max");
        registerRequest.setLastName("Mustermann");
        registerRequest.setSpokenLanguageIds(Collections.singleton(1L));
        registerRequest.setLearningLanguageIds(Collections.singleton(2L));
        registerRequest.setEmail("max.mustermann@gmx.at");
        registerRequest.setPassword("secretPW123");
        authenticationController.register(registerRequest);

        verify(userRepository, times(1)).save(any());
    }

    @Test
    public void testRegisterUserExists() {

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setFirstName("Max");
        registerRequest.setLastName("Mustermann");
        registerRequest.setEmail("max.mustermann@gmx.net");
        registerRequest.setPassword("secretPW123");

        assertThrows(BadCredentialsException.class, () -> authenticationController.register(registerRequest));
        verify(userRepository, times(0)).save(any());
    }

    @Test
    public void testLoginSuccessful() {
        LoginRequest successRequest = new LoginRequest("max.mustermann@gmx.net", "secret123");
        AuthSuccess result = authenticationController.login(successRequest);

        assertEquals(1L, result.getUserId());
    }

    @Test
    public void testLoginFailedWrongPassword() {
        LoginRequest successRequest = new LoginRequest("max.mustermann@gmx.net", "secret123_");
        assertThrows(BadCredentialsException.class, () -> authenticationController.login(successRequest));
    }

    @Test
    public void testLoginFailedWrongEmail() {
        LoginRequest successRequest = new LoginRequest("max.mustermann@gmx.at", "secret123");
        assertThrows(BadCredentialsException.class, () -> authenticationController.login(successRequest));
    }

    @Test
    public void testGetProfileNotFound() {
        assertThrows(NoSuchElementException.class, () -> authenticationController.getProfile(2L));
    }

    @Test
    public void testGetProfileFound() {
        UserDto result = authenticationController.getProfile(1L);
        assertNotNull(result);
    }

    public static class PasswordEncoderMock implements PasswordEncoder {
        @Override
        public String encode(CharSequence charSequence) {
            return charSequence.toString() + "-encoded";
        }

        @Override
        public boolean matches(CharSequence charSequence, String s) {
            return this.encode(charSequence).equals(s);
        }
    }
}
