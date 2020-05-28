package at.heapheaparray.langbuddy.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class BasicControllerSmokeTests {
    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private MatchingController matchingController;
    @Autowired
    private StorageController storageController;

    @Test
    public void contextLoads() throws Exception {
        assertThat(authenticationController).isNotNull();
        assertThat(matchingController).isNotNull();
        assertThat(storageController).isNotNull();
    }
}