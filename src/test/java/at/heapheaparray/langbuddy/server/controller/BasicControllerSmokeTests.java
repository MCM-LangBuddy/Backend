package at.heapheaparray.langbuddy.server.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
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
