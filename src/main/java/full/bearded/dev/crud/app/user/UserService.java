package full.bearded.dev.crud.app.user;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository) {

        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public User getUserById(final Long id) {

        return userRepository.findById(id)
                             .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    public User createUser(final User user) {

        return userRepository.save(user);
    }

    public User updateUser(final Long id, final User updatedUser) {

        final var user = getUserById(id);
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        return userRepository.save(user);
    }

    public void deleteUser(final Long id) {

        userRepository.deleteById(id);
    }
}
