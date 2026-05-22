package full.bearded.dev.crud.app.user;

import full.bearded.dev.crud.app.user.model.User;
import full.bearded.dev.crud.app.user.model.UserCreateRequest;
import full.bearded.dev.crud.app.user.model.UserResponse;
import full.bearded.dev.crud.app.user.model.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private final PasswordEncoder passwordEncoder;

    public UserMapper(final PasswordEncoder passwordEncoder) {

        this.passwordEncoder = passwordEncoder;
    }

    public User toEntity(final UserCreateRequest request, final UserRole userRole) {

        return User.builder()
                   .name(request.name())
                   .email(request.email())
                   .age(request.age())
                   .password(passwordEncoder.encode(request.password()))
                   .role(userRole)
                   .build();
    }

    public UserResponse toResponse(final User user) {

        return new UserResponse(user.getId(), user.getName(), user.getEmail(), user.getAge());
    }
}
