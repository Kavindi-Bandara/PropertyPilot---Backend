package property.com.example.collab.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(max = 100) String fullName,
        @NotBlank @Email @Size(max = 150) String email,
        @Pattern(regexp = "^[0-9+()\\-\\s]{7,20}$", message = "must be a valid phone number") String phone,
        @NotBlank @Size(min = 8, max = 72) String password
) {
}
