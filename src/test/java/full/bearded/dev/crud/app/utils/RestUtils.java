package full.bearded.dev.crud.app.utils;

import static full.bearded.dev.crud.app.utils.TestConstants.ADMIN_PASSWORD;
import static full.bearded.dev.crud.app.utils.TestConstants.ADMIN_USERNAME;

import java.util.List;

import full.bearded.dev.crud.app.user.model.UserCreateRequest;
import full.bearded.dev.crud.app.user.model.UserResponse;
import full.bearded.dev.crud.app.user.model.UserUpdateRequest;
import lombok.experimental.UtilityClass;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@UtilityClass
public final class RestUtils {

    private static final String USERS_API_PATH = "/api/users";

    public static ResponseEntity<List<UserResponse>> getAllUsers(final TestRestTemplate restTemplate) {

        return restTemplate.withBasicAuth(ADMIN_USERNAME, ADMIN_PASSWORD)
                           .exchange(USERS_API_PATH,
                                     HttpMethod.GET,
                                     null,
                                     new ParameterizedTypeReference<>() {});
    }

    public static ResponseEntity<UserResponse> getUserById(final TestRestTemplate restTemplate,
                                                     final long userId) {

        return restTemplate.withBasicAuth(ADMIN_USERNAME, ADMIN_PASSWORD)
                           .getForEntity(USERS_API_PATH + "/" + userId, UserResponse.class);
    }

    public static ResponseEntity<UserResponse> createUser(final TestRestTemplate restTemplate,
                                                          final UserCreateRequest userCreateRequest) {

        return restTemplate.withBasicAuth(ADMIN_USERNAME, ADMIN_PASSWORD)
                           .postForEntity(USERS_API_PATH, userCreateRequest, UserResponse.class);
    }

    public static ResponseEntity<UserResponse> updateUser(final TestRestTemplate restTemplate,
                                                          final long userId,
                                                          final UserUpdateRequest userUpdateRequest) {

        return restTemplate.withBasicAuth(ADMIN_USERNAME, ADMIN_PASSWORD)
                           .exchange(USERS_API_PATH + "/" + userId,
                                     HttpMethod.PUT,
                                     new HttpEntity<>(userUpdateRequest),
                                     UserResponse.class);
    }

    public static void deleteUser(final TestRestTemplate restTemplate, final long userId) {

        restTemplate.withBasicAuth(ADMIN_USERNAME, ADMIN_PASSWORD)
                    .delete(USERS_API_PATH + "/" + userId);
    }
}
