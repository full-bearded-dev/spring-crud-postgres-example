package full.bearded.dev.crud.app.user.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class UserUpdateRequest {

    @NotNull(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotNull(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;

    @Min(value = 18, message = "Age should not be less than 18")
    @Max(value = 150, message = "Age should not be greater than 150")
    private int age;

    public UserUpdateRequest(final String name, final String email, final int age) {

        this.name = name;
        this.email = email;
        this.age = age;
    }

    public UserUpdateRequest() {}

    public String getName() {

        return name;
    }

    public String getEmail() {

        return email;
    }

    public int getAge() {

    return age;
}}