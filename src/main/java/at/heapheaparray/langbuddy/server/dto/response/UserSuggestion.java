package at.heapheaparray.langbuddy.server.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserSuggestion {
    private String firstName;
    private String emailAddress;
    private Long userId;
    private String profilePictureUrl;
    private Set<Language> spokenLanguages;
    private Set<Language> learningLanguages;
}
