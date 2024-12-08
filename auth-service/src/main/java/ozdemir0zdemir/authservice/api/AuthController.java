package ozdemir0zdemir.authservice.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ozdemir0zdemir.authservice.domain.AuthService;

// TODO: Implement request models
@RestController
@RequestMapping("/api/v1/auth")
public record AuthController(AuthService authService) {


    @PostMapping("/login")
    void login(String username, String password) {
    }

    @PostMapping("/register")
    void register(String username, String password) {
    }

    @PostMapping("/refresh")
    void refresh(String refreshTokenId) {
    }
}
