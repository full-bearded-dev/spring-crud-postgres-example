package full.bearded.dev.crud.app.user;

import java.util.List;

import full.bearded.dev.crud.app.user.model.UserCreateRequest;
import full.bearded.dev.crud.app.user.model.UserResponse;
import full.bearded.dev.crud.app.user.model.UserUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "CRUD operations for managing users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    public UserController(final UserService userService, final UserMapper userMapper) {

        this.userService = userService;
        this.userMapper = userMapper;
    }

    @Operation(summary = "Get all users", description = "Returns a list of all users stored in the database")
    @ApiResponse(responseCode = "200", description = "List of users returned successfully")
    @GetMapping
    public List<UserResponse> getAllUsers() {

        return userService.getAllUsers().stream()
                          .map(userMapper::toResponse)
                          .toList();
    }

    @Operation(summary = "Get a user by their ID", description = "Returns a user with the specified ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User found and returned"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/{id}")
    public UserResponse getUserById(@PathVariable("id") final Long id) {

        final var userById = userService.getUserById(id);
        return userMapper.toResponse(userById);
    }

    @Operation(summary = "Create a new user", description = "Creates and returns the newly created user")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PostMapping
    public UserResponse createUser(@Valid @RequestBody final UserCreateRequest user) {

        final var newUser = userService.createUser(user);
        return userMapper.toResponse(newUser);
    }

    @Operation(summary = "Update a user", description = "Updates a user by ID with the provided data")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    @PutMapping("/{id}")
    public UserResponse updateUser(@PathVariable("id") final Long id,
                                   @Valid @RequestBody final UserUpdateRequest updatedUser) {

        final var updateUser = userService.updateUser(id, updatedUser);
        return userMapper.toResponse(updateUser);
    }

    @Operation(summary = "Delete a user", description = "Deletes a user by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") final Long id) {

        userService.deleteUser(id);
    }
}
