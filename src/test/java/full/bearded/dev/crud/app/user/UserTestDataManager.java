package full.bearded.dev.crud.app.user;

import full.bearded.dev.crud.app.user.model.User;
import full.bearded.dev.crud.app.user.model.UserRole;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Profile("test")
@Component
public class UserTestDataManager {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserTestDataManager(UserRepository userRepository, final PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void saveNewUser(final User user) {

        userRepository.save(user.toBuilder().password(passwordEncoder.encode(user.getPassword())).build());
    }

    public void deleteAllNonAdminUsers() {

        userRepository.findAll().forEach(user -> {

            if (user.getRole() != UserRole.ADMIN) {
                userRepository.delete(user);
            }
        });
    }
}
