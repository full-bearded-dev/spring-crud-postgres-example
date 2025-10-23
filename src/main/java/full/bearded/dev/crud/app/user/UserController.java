package full.bearded.dev.crud.app.user;

import java.util.List;

import full.bearded.dev.crud.app.user.model.UserCreateRequest;
import full.bearded.dev.crud.app.user.model.UserResponse;
import full.bearded.dev.crud.app.user.model.UserUpdateRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(final UserService userService, final UserMapper userMapper) {

        this.userService = userService;
        this.userMapper = userMapper;
    }

    @GetMapping
    public List<UserResponse> getAllUsers() {

        return userService.getAllUsers().stream()
                          .map(userMapper::toResponse)
                          .toList();
    }

    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable("id") final Long id) {

        final var userById = userService.getUserById(id);
        return userMapper.toResponse(userById);
    }

    @PostMapping
    public UserResponse createUser(@Valid @RequestBody final UserCreateRequest user) {

        final var newUser = userService.createUser(user);
        return userMapper.toResponse(newUser);
    }

    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable("id") final Long id,
                                   @Valid @RequestBody final UserUpdateRequest updatedUser) {

        final var updateUser = userService.updateUser(id, updatedUser);
        return userMapper.toResponse(updateUser);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") final Long id) {

        userService.deleteUser(id);
    }
}
