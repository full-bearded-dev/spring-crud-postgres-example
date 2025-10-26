package full.bearded.dev.crud.app.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import java.util.List;

import full.bearded.dev.crud.app.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    //TODO: add more tests

    @Mock private UserMapper userMapper;
    @Mock private UserRepository userRepository;

    private UserService underTest;

    @BeforeEach
    void setUp() {

        underTest = new UserService(userMapper, userRepository);
    }

    @Test
    void getAllUsersReturnsListOfStoredUsersFromUserRepository() {

        final var user1 = new User(1L, "user1", "user1@email.com", 20);
        final var user2 = new User(2L, "user2", "user2@email.com", 30);
        doReturn(List.of(user1, user2)).when(userRepository).findAll();

        assertThat(underTest.getAllUsers()).containsExactlyInAnyOrder(user1, user2);
    }

    //More unit tests...
}