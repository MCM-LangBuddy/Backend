package at.heapheaparray.langbuddy.server.controller;

import at.heapheaparray.langbuddy.server.dao.models.User;
import at.heapheaparray.langbuddy.server.dao.repositories.UserRepository;
import at.heapheaparray.langbuddy.server.dto.response.FileUploadResult;
import at.heapheaparray.langbuddy.server.services.interfaces.StorageClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@RestController
@RequestMapping("/api/storage")
public class StorageController {
    private final StorageClient storageClient;
    private final UserRepository userRepository;

    @Autowired
    StorageController(StorageClient storageClient, UserRepository userRepository) {
        this.storageClient = storageClient;
        this.userRepository = userRepository;
    }

    @PostMapping("/uploadProfilePicture/{userId}")
    public FileUploadResult uploadProfilePicture(@RequestPart(value = "file") MultipartFile file,
                                                 @PathVariable(name = "userId") Long userId) {

        User user = userRepository.findById(userId).orElseThrow();

        FileUploadResult result = new FileUploadResult();
        result.setOriginalFileName(file.getOriginalFilename());

        String filename = generateFileName(file, userId);
        result.setFileURL(this.storageClient.uploadProfilePicture(file, filename));
        result.setNewFileName(filename);

        if (result.getFileURL() != null) {
            user.setProfilePictureUrl(result.getFileURL());
        }

        this.userRepository.save(user);

        return result;
    }

    private String generateFileName(MultipartFile multiPart, Long userId) {
        return userId + "-" + new Date().getTime() + multiPart.getOriginalFilename()
                .substring(multiPart.getOriginalFilename().lastIndexOf(".")).toLowerCase();
    }
}
