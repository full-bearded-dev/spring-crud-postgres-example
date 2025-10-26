package full.bearded.dev.crud.app.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import full.bearded.dev.crud.app.user.model.UserCreateRequest;
import full.bearded.dev.crud.app.user.model.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
class UserIntegrationTest {

    //TODO: add more tests

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

        final var userCreateRequest = new UserCreateRequest("user", "user@email.com", 20);

        restTemplate.postForEntity("/api/users", userCreateRequest, UserResponse.class);

        final var userResponseEntity = restTemplate.exchange("/api/users",
                                                             HttpMethod.GET,
                                                             null,
                                                             new ParameterizedTypeReference<List<UserResponse>>() {});

        final List<UserResponse> body = userResponseEntity.getBody();

        assertThat(userResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(body).isNotNull();
        assertThat(body.size()).isEqualTo(1);

        assertThat(body.getFirst().getName()).isEqualTo("user");
        assertThat(body.getFirst().getEmail()).isEqualTo("user@email.com");
        assertThat(body.getFirst().getAge()).isEqualTo(20);
    }
}