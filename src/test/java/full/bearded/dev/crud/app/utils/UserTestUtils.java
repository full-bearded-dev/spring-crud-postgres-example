package full.bearded.dev.crud.app.utils;

import static full.bearded.dev.crud.app.utils.RandomTestUtils.randomAge;
import static full.bearded.dev.crud.app.utils.RandomTestUtils.randomEmail;
import static full.bearded.dev.crud.app.utils.RandomTestUtils.randomId;
import static full.bearded.dev.crud.app.utils.RandomTestUtils.randomString;

import full.bearded.dev.crud.app.user.model.User;
import full.bearded.dev.crud.app.user.model.UserCreateRequest;
import full.bearded.dev.crud.app.user.model.UserResponse;
import full.bearded.dev.crud.app.user.model.UserRole;
import full.bearded.dev.crud.app.user.model.UserUpdateRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class UserTestUtils {

    public static User randomUser(final long id) {

        return new User(id, randomString(10), randomEmail(), randomAge(), randomString(10), UserRole.USER);
    }

    public static User randomUserWithNullId() {

        return new User(null, randomString(10), randomEmail(), randomAge(), randomString(10), UserRole.USER);
    }

    public static User randomUser() {

        return randomUser(randomId());
    }

    public static User from(final UserCreateRequest userCreateRequest) {

        return new User(randomId(),
                        userCreateRequest.name(),
                        userCreateRequest.email(),
                        userCreateRequest.age(),
                        randomString(10),
                        UserRole.USER);
    }

    public static User from(final long id, final UserUpdateRequest userUpdateRequest) {

        return new User(id,
                        userUpdateRequest.name(),
                        userUpdateRequest.email(),
                        userUpdateRequest.age(),
                        randomString(10),
                        UserRole.USER);
    }

    public static UserResponse from(final User user) {

        return new UserResponse(user.getId(),
                                user.getName(),
                                user.getEmail(),
                                user.getAge());
    }

    public static UserCreateRequest randomUserCreateRequest() {

        return new UserCreateRequest(randomString(10), randomEmail(), randomAge());
    }

    public static UserUpdateRequest randomUserUpdateRequest() {

        return new UserUpdateRequest(randomString(10), randomEmail(), randomAge());
    }

}
