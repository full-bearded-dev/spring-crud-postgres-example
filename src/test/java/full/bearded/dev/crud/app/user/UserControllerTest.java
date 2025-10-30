package full.bearded.dev.crud.app.user;

import static full.bearded.dev.crud.app.utils.RandomTestUtils.randomAge;
import static full.bearded.dev.crud.app.utils.RandomTestUtils.randomEmail;
import static full.bearded.dev.crud.app.utils.RandomTestUtils.randomString;
import static full.bearded.dev.crud.app.utils.TestUtils.asJsonString;
import static full.bearded.dev.crud.app.utils.UserTestUtils.from;
import static full.bearded.dev.crud.app.utils.UserTestUtils.randomUser;
import static full.bearded.dev.crud.app.utils.UserTestUtils.randomUserCreateRequest;
import static full.bearded.dev.crud.app.utils.UserTestUtils.randomUserUpdateRequest;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.refEq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.stream.Stream;

import full.bearded.dev.crud.app.exception.UserNotFoundException;
import full.bearded.dev.crud.app.user.model.User;
import full.bearded.dev.crud.app.user.model.UserCreateRequest;
import full.bearded.dev.crud.app.user.model.UserUpdateRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private static final String USERS_API_PATH = "/api/users";
    private static final User USER_1 = randomUser(1L);
    private static final User USER_2 = randomUser(2L);

    @Autowired private MockMvc mockMvc;

    @MockitoBean private UserService userService;
    @MockitoBean private UserMapper userMapper;

    @Test
    void getAllUsersReturnsListOfUserResponses() throws Exception {

        final var userResponse1 = from(USER_1);
        final var userResponse2 = from(USER_2);

        doReturn(List.of(USER_1, USER_2)).when(userService).getAllUsers();
        doReturn(userResponse1).when(userMapper).toResponse(USER_1);
        doReturn(userResponse2).when(userMapper).toResponse(USER_2);

        mockMvc.perform(get(USERS_API_PATH))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.length()").value(2))
               .andExpect(jsonPath("$[0].name").value(USER_1.getName()))
               .andExpect(jsonPath("$[1].name").value(USER_2.getName()));
    }

    @Test
    void getUserByIdReturnsUserResponseWhenUserExists() throws Exception {

        final var userResponse = from(USER_1);

        doReturn(USER_1).when(userService).getUserById(USER_1.getId());
        doReturn(userResponse).when(userMapper).toResponse(USER_1);

        mockMvc.perform(get(USERS_API_PATH + "/" + USER_1.getId()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(USER_1.getId()))
               .andExpect(jsonPath("$.name").value(USER_1.getName()))
               .andExpect(jsonPath("$.email").value(USER_1.getEmail()))
               .andExpect(jsonPath("$.age").value(USER_1.getAge()));
    }

    @Test
    void createUserCreatesUserAndReturnsCreatedUserResponse() throws Exception {

        final var userCreateRequest = randomUserCreateRequest();
        final var user = from(userCreateRequest);
        final var userResponse = from(user);

        doReturn(user).when(userService).createUser(refEq(userCreateRequest));
        doReturn(userResponse).when(userMapper).toResponse(user);

        mockMvc.perform(post(USERS_API_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(userCreateRequest)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(user.getId()))
               .andExpect(jsonPath("$.name").value(user.getName()))
               .andExpect(jsonPath("$.email").value(user.getEmail()))
               .andExpect(jsonPath("$.age").value(user.getAge()));
    }

    @Test
    void updateUserUpdatesUserAndReturnsUpdatedUserResponse() throws Exception {

        final var userId = 1L;
        final var userUpdateRequest = randomUserUpdateRequest();
        final var updatedUser = from(userId, userUpdateRequest);
        final var userResponse = from(updatedUser);

        doReturn(updatedUser).when(userService).updateUser(eq(userId), refEq(userUpdateRequest));
        doReturn(userResponse).when(userMapper).toResponse(updatedUser);

        mockMvc.perform(put(USERS_API_PATH + "/" + userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(userUpdateRequest)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(userId))
               .andExpect(jsonPath("$.name").value(updatedUser.getName()))
               .andExpect(jsonPath("$.email").value(updatedUser.getEmail()))
               .andExpect(jsonPath("$.age").value(updatedUser.getAge()));
    }

    @Test
    void deleteUserCallsUserServiceAndReturns204() throws Exception {

        final var userId = 1L;
        mockMvc.perform(delete(USERS_API_PATH + "/" + userId))
               .andExpect(status().isOk());

        verify(userService).deleteUser(userId);
    }

    @Test
    void getUserByIdThrowsUserNotFoundReturns404WithErrorMessage() throws Exception {
        final var userId = 999L;
        doThrow(new UserNotFoundException("User not found with ID: " + userId))
                .when(userService).getUserById(userId);

        mockMvc.perform(get(USERS_API_PATH + "/" + userId))
               .andExpect(status().isNotFound())
               .andExpect(jsonPath("$.status").value(HttpStatus.NOT_FOUND.value()))
               .andExpect(jsonPath("$.error").value("User not found with ID: " + userId));
    }

    @MethodSource("invalidUserCreateRequests")
    @ParameterizedTest
    void createUserWithInvalidFieldsReturnsBadRequestAndErrorMessage(final UserCreateRequest userCreateRequest,
                                                                     final String expression,
                                                                     final String expectedErrorMessage) throws
                                                                                                        Exception {

        mockMvc.perform(post(USERS_API_PATH)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(userCreateRequest)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
               .andExpect(jsonPath(expression).value(expectedErrorMessage));
    }

    @MethodSource("invalidUserUpdateRequests")
    @ParameterizedTest
    void updateUserWithInvalidFieldsReturnsBadRequestAndErrorMessage(final UserUpdateRequest userUpdateRequest,
                                                                     final String expression,
                                                                     final String expectedErrorMessage) throws
                                                                                                        Exception {
        final var userId = 1L;

        mockMvc.perform(put(USERS_API_PATH + "/" + userId)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(asJsonString(userUpdateRequest)))
               .andExpect(status().isBadRequest())
               .andExpect(jsonPath("$.status").value(HttpStatus.BAD_REQUEST.value()))
               .andExpect(jsonPath(expression).value(expectedErrorMessage));
    }

    private static Stream<Arguments> invalidUserCreateRequests() {

        final var validName = randomString(10);
        final var validEmail = randomEmail();
        final var validAge = randomAge();

        final var nameExpression = "$.errors.name";
        final var emailExpression = "$.errors.email";
        final var ageExpression = "$.errors.age";

        return Stream.of(
                Arguments.of(new UserCreateRequest(null, validEmail, validAge),
                             nameExpression,
                             "Name is required"),
                Arguments.of(new UserCreateRequest(randomString(200), validEmail, validAge),
                             nameExpression,
                             "Name must be between 2 and 100 characters"),
                Arguments.of(new UserCreateRequest(validName, null, validAge),
                             emailExpression,
                             "Email is required"),
                Arguments.of(new UserCreateRequest(validName, "invalid", validAge),
                             emailExpression,
                             "Email must be valid"),
                Arguments.of(new UserCreateRequest(validName, "invalid@", validAge),
                             emailExpression,
                             "Email must be valid"),
                Arguments.of(new UserCreateRequest(validName, "invalid@.com", validAge),
                             emailExpression,
                             "Email must be valid"),
                Arguments.of(new UserCreateRequest(validName, validEmail, -1),
                             ageExpression,
                             "Age should not be less than 18"),
                Arguments.of(new UserCreateRequest(validName, validEmail, 200),
                             ageExpression,
                             "Age should not be greater than 150"),
                Arguments.of(new UserCreateRequest(validName, validEmail, 10),
                             ageExpression,
                             "Age should not be less than 18")
        );
    }

    private static Stream<Arguments> invalidUserUpdateRequests() {

        final var validName = randomString(10);
        final var validEmail = randomEmail();
        final var validAge = randomAge();

        final var nameExpression = "$.errors.name";
        final var emailExpression = "$.errors.email";
        final var ageExpression = "$.errors.age";

        return Stream.of(
                Arguments.of(new UserUpdateRequest(null, validEmail, validAge),
                             nameExpression,
                             "Name is required"),
                Arguments.of(new UserUpdateRequest(randomString(200), validEmail, validAge),
                             nameExpression,
                             "Name must be between 2 and 100 characters"),
                Arguments.of(new UserUpdateRequest(validName, null, validAge),
                             emailExpression,
                             "Email is required"),
                Arguments.of(new UserUpdateRequest(validName, "invalid", validAge),
                             emailExpression,
                             "Email must be valid"),
                Arguments.of(new UserUpdateRequest(validName, "invalid@", validAge),
                             emailExpression,
                             "Email must be valid"),
                Arguments.of(new UserUpdateRequest(validName, "invalid@.com", validAge),
                             emailExpression,
                             "Email must be valid"),
                Arguments.of(new UserUpdateRequest(validName, validEmail, -1),
                             ageExpression,
                             "Age should not be less than 18"),
                Arguments.of(new UserUpdateRequest(validName, validEmail, 200),
                             ageExpression,
                             "Age should not be greater than 150"),
                Arguments.of(new UserUpdateRequest(validName, validEmail, 10),
                             ageExpression,
                             "Age should not be less than 18")
        );
    }
}