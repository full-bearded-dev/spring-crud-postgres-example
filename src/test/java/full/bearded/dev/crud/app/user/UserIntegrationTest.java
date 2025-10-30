package full.bearded.dev.crud.app.user;

import static full.bearded.dev.crud.app.utils.RestUtils.createUser;
import static full.bearded.dev.crud.app.utils.RestUtils.deleteUser;
import static full.bearded.dev.crud.app.utils.RestUtils.getAllUsers;
import static full.bearded.dev.crud.app.utils.RestUtils.getUserById;
import static full.bearded.dev.crud.app.utils.RestUtils.updateUser;
import static full.bearded.dev.crud.app.utils.UserTestUtils.randomUserCreateRequest;
import static full.bearded.dev.crud.app.utils.UserTestUtils.randomUserUpdateRequest;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

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
        assertThat(body.size()).isEqualTo(1);
        assertThat(body.getFirst().getName()).isEqualTo(userCreateRequest.getName());
        assertThat(body.getFirst().getEmail()).isEqualTo(userCreateRequest.getEmail());
        assertThat(body.getFirst().getAge()).isEqualTo(userCreateRequest.getAge());
    }

    @Test
    void shouldUpdateUserSuccessfully() {

        final var userCreateRequest = randomUserCreateRequest();
        final var createdUserResponse = createUser(restTemplate, userCreateRequest);
        final var createdUser = createdUserResponse.getBody();

        assertThat(createdUser).isNotNull();

        final var userUpdateRequest = randomUserUpdateRequest();
        final var updatedUserResponse = updateUser(restTemplate, createdUser.getId(), userUpdateRequest);
        final var body = updatedUserResponse.getBody();

        assertThat(updatedUserResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(body).isNotNull();
        assertThat(body.getName()).isEqualTo(userUpdateRequest.getName());
        assertThat(body.getEmail()).isEqualTo(userUpdateRequest.getEmail());
        assertThat(body.getAge()).isEqualTo(userUpdateRequest.getAge());
    }

    @Test
    void shouldDeleteUserAndReturnNotFoundOnSubsequentGet() {

        final var userCreateRequest = randomUserCreateRequest();
        final var createdUserResponse = createUser(restTemplate, userCreateRequest);
        final var createdUser = createdUserResponse.getBody();

        assertThat(createdUser).isNotNull();

        deleteUser(restTemplate, createdUser.getId());

        final var userResponse = getUserById(restTemplate, createdUser.getId());
        assertThat(userResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}