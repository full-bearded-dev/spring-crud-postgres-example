package full.bearded.dev.crud.app.user;

import static full.bearded.dev.crud.app.utils.UserTestUtils.from;
import static full.bearded.dev.crud.app.utils.UserTestUtils.randomUser;
import static full.bearded.dev.crud.app.utils.UserTestUtils.randomUserCreateRequest;
import static full.bearded.dev.crud.app.utils.UserTestUtils.randomUserUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;

import full.bearded.dev.crud.app.exception.UserNotFoundException;
import full.bearded.dev.crud.app.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    private static final long NON_EXISTENT_ID = 999L;
    private static final User USER_1 = randomUser();
    private static final User USER_2 = randomUser();

    @Mock private UserMapper userMapper;
    @Mock private UserRepository userRepository;

    private UserService underTest;

    @BeforeEach
    void setUp() {

        underTest = new UserService(userMapper, userRepository);
    }

    @Test
    void getAllUsersReturnsListOfStoredUsersFromUserRepository() {

        doReturn(List.of(USER_1, USER_2)).when(userRepository).findAll();

        assertThat(underTest.getAllUsers()).containsExactlyInAnyOrder(USER_1, USER_2);
    }

    @Test
    void getUserByIdReturnsUserWhenExists() {

        final var user1Id = USER_1.getId();
        doReturn(Optional.of(USER_1)).when(userRepository).findById(user1Id);

        assertThat(underTest.getUserById(user1Id)).isEqualTo(USER_1);
    }

    @Test
    void getUserByIdThrowsExceptionWhenUserDoesNotExist() {

        doReturn(Optional.empty()).when(userRepository).findById(NON_EXISTENT_ID);

        assertThatThrownBy(() -> underTest.getUserById(NON_EXISTENT_ID))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with ID: " + NON_EXISTENT_ID);
    }

    @Test
    void createUserMapsAndSavesNewUser() {

        final var request = randomUserCreateRequest();
        final var mappedUser = from(request);

        doReturn(mappedUser).when(userMapper).toEntity(request);
        doReturn(mappedUser).when(userRepository).save(mappedUser);

        assertThat(underTest.createUser(request)).isEqualTo(mappedUser);
        verify(userMapper).toEntity(request);
        verify(userRepository).save(mappedUser);
    }

    @Test
    void updateUserUpdatesExistingUserFieldsAndSaves() {

        final var userId = 1L;
        final var existingUser = randomUser(userId);
        final var updatedRequest = randomUserUpdateRequest();

        doReturn(Optional.of(existingUser)).when(userRepository).findById(userId);
        doReturn(existingUser).when(userRepository).save(existingUser);

        final var result = underTest.updateUser(userId, updatedRequest);

        assertThat(result.getName()).isEqualTo(updatedRequest.getName());
        assertThat(result.getEmail()).isEqualTo(updatedRequest.getEmail());
        assertThat(result.getAge()).isEqualTo(updatedRequest.getAge());
        verify(userRepository).save(existingUser);
    }

    @Test
    void updateUserThrowsExceptionWhenUserDoesNotExist() {

        final var request = randomUserUpdateRequest();

        doReturn(Optional.empty()).when(userRepository).findById(NON_EXISTENT_ID);

        assertThatThrownBy(() -> underTest.updateUser(NON_EXISTENT_ID, request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with ID: " + NON_EXISTENT_ID);
    }

    @Test
    void deleteUserDeletesUserById() {

        final var userIdToDelete = 1L;
        underTest.deleteUser(userIdToDelete);

        verify(userRepository).deleteById(userIdToDelete);
    }
}