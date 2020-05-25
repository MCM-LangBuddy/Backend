package at.heapheaparray.langbuddy.server.dto.requests;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}