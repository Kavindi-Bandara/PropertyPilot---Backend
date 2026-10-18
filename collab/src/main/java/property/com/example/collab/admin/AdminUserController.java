package property.com.example.collab.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import property.com.example.collab.common.ApiException;
import property.com.example.collab.user.User;
import property.com.example.collab.user.UserRepository;
import property.com.example.collab.user.UserResponse;

import java.util.List;

/** User management for admins. Also guarded by the /api/admin/** rule in SecurityConfig. */
@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserRepository userRepository;

    public record StatusRequest(boolean active) {
    }

    @GetMapping
    public List<UserResponse> list() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id) {
        return UserResponse.from(find(id));
    }

    @PatchMapping("/{id}/status")
    @Transactional
    public UserResponse setStatus(@PathVariable Long id, @RequestBody StatusRequest request,
                                  @AuthenticationPrincipal Jwt jwt) {
        User user = find(id);
        if (!request.active() && user.getEmail().equalsIgnoreCase(jwt.getSubject())) {
            throw new ApiException(HttpStatus.BAD_REQUEST, "You cannot deactivate your own account.");
        }
        user.setActive(request.active());
        return UserResponse.from(userRepository.save(user));
    }

    private User find(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "User not found."));
    }
}
