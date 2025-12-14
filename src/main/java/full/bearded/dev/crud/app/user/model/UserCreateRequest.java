package full.bearded.dev.crud.app.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to create a new user")
public record UserCreateRequest(

        @Schema(description = "Full name of the user", example = "Jon Smith", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Name is required")
        @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
        String name,

        @Schema(description = "Email address of the user", example = "jon@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Email is required")
        @Email(message = "Email must be valid")
        String email,

        @Schema(description = "Age of the user", example = "30", requiredMode = Schema.RequiredMode.REQUIRED)
        @Min(value = 18, message = "Age should not be less than 18")
        @Max(value = 150, message = "Age should not be greater than 150")
        int age) {}
