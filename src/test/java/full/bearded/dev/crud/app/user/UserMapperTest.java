package full.bearded.dev.crud.app.user;

import static full.bearded.dev.crud.app.utils.UserTestUtils.randomUser;
import static full.bearded.dev.crud.app.utils.UserTestUtils.randomUserCreateRequest;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserMapperTest {

    private UserMapper underTest;

    @BeforeEach
    void setUp() {

        underTest = new UserMapper();
    }

    @Test
    void toEntityMapsFieldsCorrectlyFromUserCreateRequest() {

        final var request = randomUserCreateRequest();

        final var result = underTest.toEntity(request);

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