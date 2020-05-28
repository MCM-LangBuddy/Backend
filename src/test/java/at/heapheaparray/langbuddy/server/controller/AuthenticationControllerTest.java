package at.heapheaparray.langbuddy.server.controller;

import at.heapheaparray.langbuddy.server.dao.models.User;
import at.heapheaparray.langbuddy.server.dao.repositories.UserRepository;
import at.heapheaparray.langbuddy.server.dto.requests.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.contains;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private PasswordEncoderMock passwordEncoderMock;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testLoginFailedNoParams() throws Exception {
        mockMvc.perform(post("/api/auth/login")).andExpect(status().isBadRequest());
    }

    @Test
    public void testLoginFailedWrongCredentials() throws Exception {

        userRepository.save(User.builder().firstName("Max")
                .lastName("Mustermann")
                .email("test@test.gmx")
                .password(passwordEncoderMock.encode("secret"))
                .build());

        LoginRequest wrongPassword = new LoginRequest("test@test.gmx", "Test1234");
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(wrongPassword)))
                .andExpect(status().isForbidden());

        LoginRequest wrongUsername = new LoginRequest("abc@test.gmx", "secret");
        mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(wrongUsername)))
                .andExpect(status().isForbidden());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
