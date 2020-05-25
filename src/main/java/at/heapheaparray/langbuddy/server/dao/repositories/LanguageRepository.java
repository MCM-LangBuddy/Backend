package at.heapheaparray.langbuddy.server.dao.repositories;

import at.heapheaparray.langbuddy.server.dao.models.Language;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LanguageRepository extends JpaRepository<Language, Long> {
}
