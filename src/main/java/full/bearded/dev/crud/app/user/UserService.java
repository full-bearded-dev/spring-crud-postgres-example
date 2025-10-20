package full.bearded.dev.crud.app.user;

import java.util.List;

import full.bearded.dev.crud.app.user.model.User;
import full.bearded.dev.crud.app.user.model.UserCreateRequest;
import full.bearded.dev.crud.app.user.model.UserUpdateRequest;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserService(final UserMapper userMapper, final UserRepository userRepository) {

        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public List<User> getAllUsers() {

        return userRepository.findAll();
    }

    public User getUserById(final Long id) {

        return userRepository.findById(id)
                             .orElseThrow(() -> new RuntimeException("User not found with ID: " + id));
    }

    public User createUser(final UserCreateRequest user) {

        final var newUser = userMapper.toEntity(user);
        return userRepository.save(newUser);
    }

    public User updateUser(final Long id, final UserUpdateRequest updatedUser) {

        final var user = getUserById(id);
        user.setName(updatedUser.getName());
        user.setEmail(updatedUser.getEmail());
        return userRepository.save(user);
    }

    public void deleteUser(final Long id) {

        userRepository.deleteById(id);
    }
}
