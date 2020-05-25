package at.heapheaparray.langbuddy.server.models.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String userName;

    private String password;
}
