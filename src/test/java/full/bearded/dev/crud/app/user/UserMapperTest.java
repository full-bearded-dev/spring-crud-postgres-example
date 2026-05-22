package full.bearded.dev.crud.app.user;

import static full.bearded.dev.crud.app.utils.UserTestUtils.randomUser;
import static full.bearded.dev.crud.app.utils.UserTestUtils.randomUserCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;

import full.bearded.dev.crud.app.user.model.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    @Mock private PasswordEncoder passwordEncoder;

    private UserMapper underTest;

    @BeforeEach
    void setUp() {

        underTest = new UserMapper(passwordEncoder);
    }

    @Test
    void toEntityMapsFieldsCorrectlyFromUserCreateRequest() {

        final var request = randomUserCreateRequest();

        doReturn(request.password()).when(passwordEncoder).encode(request.password());

        final var result = underTest.toEntity(request, UserRole.USER);

        assertThat(result.getName()).isEqualTo(request.name());
        assertThat(result.getEmail()).isEqualTo(request.email());
        assertThat(result.getAge()).isEqualTo(request.age());
    }

    @Test
    void toResponseMapsFieldsCorrectlyFromUserEntity() {

        final var user = randomUser();
        final var result = underTest.toResponse(user);

        assertThat(result.id()).isEqualTo(user.getId());
        assertThat(result.name()).isEqualTo(user.getName());
        assertThat(result.email()).isEqualTo(user.getEmail());
        assertThat(result.age()).isEqualTo(user.getAge());
    }
}