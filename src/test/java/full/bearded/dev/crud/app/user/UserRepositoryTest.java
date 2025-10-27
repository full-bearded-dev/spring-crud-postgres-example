package full.bearded.dev.crud.app.user;

import static org.assertj.core.api.Assertions.assertThat;

import full.bearded.dev.crud.app.user.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void userRepositoryTest() {

        final var user = new User(null, "fbd", "fbd@example.com", 40);
        final var savedUser = userRepository.save(user);

        final var foundUserList = userRepository.findAll();

        assertThat(foundUserList).hasSize(1);
        assertThat(foundUserList.get(0)).isEqualTo(savedUser);
    }
}