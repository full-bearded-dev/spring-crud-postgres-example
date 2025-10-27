package full.bearded.dev.crud.app.utils;

import static full.bearded.dev.crud.app.utils.RandomTestUtils.randomAge;
import static full.bearded.dev.crud.app.utils.RandomTestUtils.randomEmail;
import static full.bearded.dev.crud.app.utils.RandomTestUtils.randomId;
import static full.bearded.dev.crud.app.utils.RandomTestUtils.randomString;

import full.bearded.dev.crud.app.user.model.User;
import full.bearded.dev.crud.app.user.model.UserCreateRequest;
import full.bearded.dev.crud.app.user.model.UserUpdateRequest;

public class UserTestUtils {

    public static User randomUser() {

        return new User(randomId(), randomString(10), randomEmail(), randomAge());
    }

    public static User from(final UserCreateRequest userCreateRequest) {

        return new User(randomId(),
                        userCreateRequest.getName(),
                        userCreateRequest.getEmail(),
                        userCreateRequest.getAge());
    }

    public static UserCreateRequest randomUserCreateRequest() {

        return new UserCreateRequest(randomString(10), randomEmail(), randomAge());
    }

    public static UserUpdateRequest randomUserUpdateRequest() {

        return new UserUpdateRequest(randomString(10), randomEmail(), randomAge());
    }

}
