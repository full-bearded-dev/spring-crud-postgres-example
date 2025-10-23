package full.bearded.dev.crud.app.user;

import full.bearded.dev.crud.app.user.model.User;
import full.bearded.dev.crud.app.user.model.UserCreateRequest;
import full.bearded.dev.crud.app.user.model.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(final UserCreateRequest request) {

        final var user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setAge(request.getAge());
        return user;
    }

    public UserResponse toResponse(final User user) {

        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getAge());
    }
}
