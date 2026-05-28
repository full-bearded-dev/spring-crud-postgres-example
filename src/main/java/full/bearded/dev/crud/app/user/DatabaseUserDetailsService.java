package full.bearded.dev.crud.app.user;

import full.bearded.dev.crud.app.exception.UserNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DatabaseUserDetailsService(final UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(final String username) {

        final var user = userRepository.findByName(username)
                                       .orElseThrow(() -> new UserNotFoundException(username));

        return User.withUsername(user.getName())
                   .password(user.getPassword())
                   .roles(user.getRole().name())
                   .build();
    }
}