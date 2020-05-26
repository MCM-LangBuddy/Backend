package at.heapheaparray.langbuddy.server.dao.models;

import at.heapheaparray.langbuddy.server.dto.response.LanguageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String shortName;
    private String clearName;

    public Language(Long id) {
        this.id = id;
    }

    public static LanguageDto toDto(Language language) {
        return LanguageDto.builder()
                .clearName(language.clearName)
                .shortName(language.shortName)
                .id(language.id)
                .build();
    }
}
