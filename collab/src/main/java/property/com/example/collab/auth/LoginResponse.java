package property.com.example.collab.auth;

/** Shape expected by the frontend's authService.login(). */
public record LoginResponse(
        String token,
        Long id,
        String name,
        String email,
        String role
) {
}
