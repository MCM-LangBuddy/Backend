package at.heapheaparray.langbuddy.server.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Language {
    private Long id;
    private String shortName;
    private String clearName;
}
