package at.heapheaparray.langbuddy.server.dto.response;

import lombok.Data;

@Data
public class FileUploadResult {
    private String originalFileName;
    private String newFileName;
    private String fileURL;
}
