package full.bearded.dev.crud.app.user;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import full.bearded.dev.crud.app.user.model.User;
import full.bearded.dev.crud.app.user.model.UserCreateRequest;
import full.bearded.dev.crud.app.user.model.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

    //TODO: add more tests

    @Autowired private MockMvc mockMvc;

    @MockitoBean private UserService userService;
    @MockitoBean private UserMapper userMapper;

    @Test
    void getAllUsersReturnsListOfUserResponses() throws Exception {

        final var user1 = new User(1L, "user1", "user1@email.com", 20);
        final var user2 = new User(2L, "user2", "user2@email.com", 30);

        final var response1 = new UserResponse(1L, "user1", "user1@email.com", 20);
        final var response2 = new UserResponse(2L, "user2", "user2@email.com", 30);

        doReturn(List.of(user1, user2)).when(userService).getAllUsers();
        doReturn(response1).when(userMapper).toResponse(user1);
        doReturn(response2).when(userMapper).toResponse(user2);

        mockMvc.perform(get("/api/users"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(2))
               .andExpect(jsonPath("$[0].name").value("user1"))
               .andExpect(jsonPath("$[1].name").value("user2"));
    }

    @Test
    void shouldReturnUserOnPost() throws Exception {

        final var userName = "user1";
        final var userEmail = "user@email.com";
        final var userAge = 20;

        final var user = new User(1L, userName, userEmail, userAge);
        final var userResponse = new UserResponse(1L, userName, userEmail, userAge);
        final var userCreateRequest = new UserCreateRequest(userName, userEmail, userAge);

        doReturn(user).when(userService).createUser(any(UserCreateRequest.class));
        doReturn(userResponse).when(userMapper).toResponse(user);

        mockMvc.perform(post("/api/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(userCreateRequest)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("user1"));
    }

    public static String asJsonString(final Object obj) {

        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}