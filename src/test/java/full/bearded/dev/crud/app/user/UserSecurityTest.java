package full.bearded.dev.crud.app.user;

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

import full.bearded.dev.crud.app.integration.IntegrationTest;
import full.bearded.dev.crud.app.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

class UserSecurityTest extends IntegrationTest {

    private static final User USER = randomUserWithNullId();

    @Autowired private MockMvc mockMvc;
    @Autowired private UserTestDataManager userTestDataManager;

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