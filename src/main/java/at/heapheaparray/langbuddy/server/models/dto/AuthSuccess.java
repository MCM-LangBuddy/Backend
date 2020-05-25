package at.heapheaparray.langbuddy.server.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthSuccess {
    private String message;
    private Integer userId;

    public AuthSuccess(String message) {
        this.message = message;
    }
}
