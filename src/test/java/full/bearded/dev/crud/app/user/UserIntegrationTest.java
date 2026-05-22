package full.bearded.dev.crud.app.user;

import static full.bearded.dev.crud.app.utils.RestUtils.createUser;
import static full.bearded.dev.crud.app.utils.RestUtils.deleteUser;
import static full.bearded.dev.crud.app.utils.RestUtils.getAllUsers;
import static full.bearded.dev.crud.app.utils.RestUtils.getUserById;
import static full.bearded.dev.crud.app.utils.RestUtils.updateUser;
import static full.bearded.dev.crud.app.utils.TestConstants.ADMIN_EMAIL;
import static full.bearded.dev.crud.app.utils.TestConstants.ADMIN_PASSWORD;
import static full.bearded.dev.crud.app.utils.TestConstants.ADMIN_USERNAME;
import static full.bearded.dev.crud.app.utils.UserTestUtils.randomUserCreateRequest;
import static full.bearded.dev.crud.app.utils.UserTestUtils.randomUserUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import full.bearded.dev.crud.app.user.model.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test-db")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void configure(final DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("app.security.admin.password", () -> ADMIN_PASSWORD);
        registry.add("app.security.admin.email", () -> ADMIN_EMAIL);
    }

    @Autowired private TestRestTemplate restTemplate;

    @Test
    void shouldCreateAndFetchUser() {

        final var userCreateRequest = randomUserCreateRequest();
        createUser(restTemplate, userCreateRequest);

        final var listOfUsersResponse = getAllUsers(restTemplate);
        final var body = listOfUsersResponse.getBody();

        assertThat(listOfUsersResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body).usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                        .isEqualTo(List.of(
                                new UserResponse(null, ADMIN_USERNAME, ADMIN_EMAIL, 0),
                                new UserResponse(null, userCreateRequest.name(), userCreateRequest.email(), userCreateRequest.age())
                        ));
    }

    @Test
    void shouldUpdateUserSuccessfully() {

        final var userCreateRequest = randomUserCreateRequest();
        final var createdUserResponse = createUser(restTemplate, userCreateRequest);
        final var createdUser = createdUserResponse.getBody();

        assertThat(createdUser).isNotNull();

        final var userUpdateRequest = randomUserUpdateRequest();
        final var updatedUserResponse = updateUser(restTemplate, createdUser.id(), userUpdateRequest);
        final var body = updatedUserResponse.getBody();

        assertThat(updatedUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body.name()).isEqualTo(userUpdateRequest.name());
        assertThat(body.email()).isEqualTo(userUpdateRequest.email());
        assertThat(body.age()).isEqualTo(userUpdateRequest.age());
    }

    @Test
    void shouldDeleteUserAndReturnNotFoundOnSubsequentGet() {

        final var userCreateRequest = randomUserCreateRequest();
        final var createdUserResponse = createUser(restTemplate, userCreateRequest);
        final var createdUser = createdUserResponse.getBody();

        assertThat(createdUser).isNotNull();

        deleteUser(restTemplate, createdUser.id());

        final var userResponse = getUserById(restTemplate, createdUser.id());
        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}