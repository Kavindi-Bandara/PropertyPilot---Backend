package property.com.example.collab.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import property.com.example.collab.user.Role;
import property.com.example.collab.user.User;
import property.com.example.collab.user.UserRepository;

import java.time.LocalDateTime;

/** Admins cannot sign up through the API, so the first one is created from the app.admin.* properties. */
@Slf4j
@Component
@RequiredArgsConstructor
public class AdminSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.email:}")
    private String email;

    @Value("${app.admin.password:}")
    private String password;

    @Value("${app.admin.full-name:Administrator}")
    private String fullName;

    @Override
    public void run(ApplicationArguments args) {
        if (email.isBlank() || password.isBlank() || userRepository.existsByEmailIgnoreCase(email)) {
            return;
        }
        User admin = new User();
        admin.setFullName(fullName);
        admin.setEmail(email.trim().toLowerCase());
        admin.setPasswordHash(passwordEncoder.encode(password));
        admin.setRole(Role.ADMIN);
        admin.setTermsAcceptedAt(LocalDateTime.now());
        userRepository.save(admin);
        log.info("Created admin account {}", admin.getEmail());
    }
}
