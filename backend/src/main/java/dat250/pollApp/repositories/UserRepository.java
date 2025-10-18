package dat250.pollApp.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import dat250.pollApp.domain.User;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByUsernameIgnoreCase(String username);
    Optional<User> findByEmailIgnoreCase(String email);
}
