package at.heapheaparray.langbuddy.server.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserDto {
    private String firstName;
    private String emailAddress;
    private Long userId;
    private String profilePictureUrl;
    private Set<LanguageDto> spokenLanguageDtos;
    private Set<LanguageDto> learningLanguageDtos;
}