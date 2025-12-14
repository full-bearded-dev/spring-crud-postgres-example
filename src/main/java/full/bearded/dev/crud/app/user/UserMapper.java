package full.bearded.dev.crud.app.user;

import full.bearded.dev.crud.app.user.model.User;
import full.bearded.dev.crud.app.user.model.UserCreateRequest;
import full.bearded.dev.crud.app.user.model.UserResponse;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(final UserCreateRequest request) {

        return User.builder()
                   .name(request.name())
                   .email(request.email())
                   .age(request.age())
                   .build();
    }

    public UserResponse toResponse(final User user) {

        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getAge());
    }
}
