package at.heapheaparray.langbuddy.server.dao.repositories;

import at.heapheaparray.langbuddy.server.dao.models.Language;
import at.heapheaparray.langbuddy.server.dao.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    List<User> findDistinctBySpokenLanguagesInAndIdNotIn(Set<Language> languages, Set<Long> blockedIds);
}
