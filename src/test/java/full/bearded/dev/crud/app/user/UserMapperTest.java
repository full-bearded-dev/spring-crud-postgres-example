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

        assertThat(result.getName()).isEqualTo(request.getName());
        assertThat(result.getEmail()).isEqualTo(request.getEmail());
        assertThat(result.getAge()).isEqualTo(request.getAge());
    }

    @Test
    void toResponseMapsFieldsCorrectlyFromUserEntity() {

        final var user = randomUser();
        final var result = underTest.toResponse(user);

        assertThat(result.getId()).isEqualTo(user.getId());
        assertThat(result.getName()).isEqualTo(user.getName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getAge()).isEqualTo(user.getAge());
    }
}