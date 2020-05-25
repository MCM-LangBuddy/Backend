package at.heapheaparray.langbuddy.server.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthSuccess {
    private String message;
    private Long userId;

    public AuthSuccess(String message) {
        this.message = message;
    }
}
