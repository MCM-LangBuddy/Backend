package at.heapheaparray.langbuddy.server.dto.requests;

import lombok.Data;

import java.util.Set;

@Data
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String userName;
    private Set<Long> spokenLanguageIds;
    private Set<Long> learningLanguageIds;
    private String password;
}
