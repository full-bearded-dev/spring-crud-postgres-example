package full.bearded.dev.crud.app.user.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object for a user")
public class UserResponse {

    @Schema(description = "Unique ID of the user", example = "1")
    private Long id;

    @Schema(description = "Full name of the user", example = "Jon Smith")
    private String name;

    @Schema(description = "Email address of the user", example = "jon@example.com")
    private String email;

    @Schema(description = "Age of the user", example = "30")
    private int age;

    public UserResponse(final Long id, final String name, final String email, final int age) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.age = age;
    }

    public UserResponse() {}

    public Long getId() {

        return id;
    }

    public String getName() {

        return name;
    }

    public String getEmail() {

        return email;
    }

    public int getAge() {

        return age;
    }
}