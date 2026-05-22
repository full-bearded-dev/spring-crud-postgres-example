package full.bearded.dev.crud.app.user;

import java.util.Optional;

import full.bearded.dev.crud.app.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByName(final String name);
}