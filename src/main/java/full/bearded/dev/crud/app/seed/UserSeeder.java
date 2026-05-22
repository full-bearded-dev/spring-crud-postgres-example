package full.bearded.dev.crud.app.seed;

import full.bearded.dev.crud.app.config.properties.AdminProperties;
import full.bearded.dev.crud.app.user.UserRepository;
import full.bearded.dev.crud.app.user.model.User;
import full.bearded.dev.crud.app.user.model.UserRole;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class UserSeeder {

    private static final String ADMIN_USERNAME = "admin";

    @Bean
    public CommandLineRunner seedAdminUser(final UserRepository userRepository,
                                           final PasswordEncoder passwordEncoder,
                                           final AdminProperties adminProperties) {

        return args -> {
            if (userRepository.findByName(ADMIN_USERNAME).isEmpty()) {
                final var admin = new User();
                admin.setName(ADMIN_USERNAME);
                admin.setPassword(passwordEncoder.encode(adminProperties.getPassword()));
                admin.setEmail(adminProperties.getEmail());
                admin.setRole(UserRole.ADMIN);
                userRepository.save(admin);
            }
        };
    }
}