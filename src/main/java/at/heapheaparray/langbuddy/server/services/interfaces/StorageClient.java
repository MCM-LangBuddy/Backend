package at.heapheaparray.langbuddy.server.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

public interface StorageClient {
    String uploadCreateCallPicture(MultipartFile file, String fileName);
}
