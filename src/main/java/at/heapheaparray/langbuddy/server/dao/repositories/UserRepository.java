package at.heapheaparray.langbuddy.server.dao.repositories;

import at.heapheaparray.langbuddy.server.dao.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);
}
