package full.bearded.dev.crud.app.user.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response object for a user")
public record UserResponse(@Schema(description = "Unique ID of the user", example = "1") Long id,
                           @Schema(description = "Full name of the user", example = "Jon Smith") String name,
                           @Schema(description = "Email address of the user", example = "jon@example.com") String email,
                           @Schema(description = "Age of the user", example = "30") int age) {
}