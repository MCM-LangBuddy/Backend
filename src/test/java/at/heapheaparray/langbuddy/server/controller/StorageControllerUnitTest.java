package at.heapheaparray.langbuddy.server.controller;

import at.heapheaparray.langbuddy.server.dao.models.User;
import at.heapheaparray.langbuddy.server.dao.repositories.UserRepository;
import at.heapheaparray.langbuddy.server.dto.response.FileUploadResult;
import at.heapheaparray.langbuddy.server.services.interfaces.StorageClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {StorageController.class})
public class StorageControllerUnitTest {
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private StorageClient storageClient;
    @Autowired
    private StorageController storageController;

    private User testUser;
    private MockMultipartFile mockFile;

    @BeforeEach
    public void setup() {
        testUser = User.builder().id(1L)
                .firstName("Max")
                .lastName("Mustermann")
                .email("max.mustermann@gmx.net")
                .build();

        mockFile = new MockMultipartFile("data", "filename.txt", "text/plain", "some xml".getBytes());

        when(userRepository.findByEmail("max.mustermann@gmx.net")).thenReturn(testUser);
        when(userRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(testUser));
    }

    @Test
    public void testUploadProfilePictureInvalidUser() {
        assertThrows(NoSuchElementException.class, () -> storageController.uploadProfilePicture(null,2L));
    }

    @Test
    public void testUploadProfilePicture() {
        when(storageClient.uploadProfilePicture(any(), any())).thenReturn("www.test.at");

        FileUploadResult result = storageController.uploadProfilePicture(mockFile, 1L);
        assertTrue(result.getNewFileName().matches("1-[0-9]+.txt"));
        assertEquals("filename.txt",result.getOriginalFileName());

        verify(userRepository).save(argThat( user -> user.getProfilePictureUrl().equals("www.test.at")));
    }
}
