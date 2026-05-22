package full.bearded.dev.crud.app.user;

import static full.bearded.dev.crud.app.utils.TestConstants.ADMIN_EMAIL;
import static full.bearded.dev.crud.app.utils.TestConstants.ADMIN_PASSWORD;
import static full.bearded.dev.crud.app.utils.TestConstants.ADMIN_USERNAME;
import static full.bearded.dev.crud.app.utils.TestConstants.USERS_API_PATH;
import static full.bearded.dev.crud.app.utils.TestUtils.asJsonString;
import static full.bearded.dev.crud.app.utils.UserTestUtils.randomUserCreateRequest;
import static full.bearded.dev.crud.app.utils.UserTestUtils.randomUserWithNullId;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import full.bearded.dev.crud.app.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
class UserSecurityTest {

    private static final User USER = randomUserWithNullId();

    @Autowired private MockMvc mockMvc;
    @Autowired private UserTestDataManager userTestDataManager;

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

    @BeforeEach
    void setUp() {

        userTestDataManager.deleteAllNonAdminUsers();
        userTestDataManager.saveNewUser(USER);
    }

    @Test
    void returnsUnauthorisedWhenNoCredentialsProvided() throws Exception {

        mockMvc.perform(get(USERS_API_PATH))
               .andExpect(status().isUnauthorized());
    }

    @Test
    void allowsAUserRoleToReadUsers() throws Exception {

        mockMvc.perform(get(USERS_API_PATH)
                                .with(httpBasic(USER.getName(), USER.getPassword())))
               .andExpect(status().isOk());
    }

    @Test
    void forbidsTheUserRoleFromCreatingUsers() throws Exception {

        final var userCreateRequest = randomUserCreateRequest();

        mockMvc.perform(post(USERS_API_PATH)
                                .with(httpBasic(USER.getName(), USER.getPassword()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(userCreateRequest)))
               .andExpect(status().isForbidden());
    }

    @Test
    void allowsTheAdminRoleToCreateUsers() throws Exception {

        final var userCreateRequest = randomUserCreateRequest();

        mockMvc.perform(post(USERS_API_PATH)
                                .with(httpBasic(ADMIN_USERNAME, ADMIN_PASSWORD))
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(userCreateRequest)))
               .andExpect(status().isOk());
    }
}