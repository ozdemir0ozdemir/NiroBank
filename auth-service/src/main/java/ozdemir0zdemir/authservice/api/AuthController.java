package ozdemir0zdemir.authservice.api;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ozdemir0ozdemir.nirobank.client.tokenclient.request.RefreshToken;
import ozdemir0ozdemir.nirobank.client.userclient.request.Login;
import ozdemir0ozdemir.nirobank.client.userclient.request.RegisterUser;
import ozdemir0zdemir.authservice.domain.AuthService;

// TODO: Implement request models
@RestController
@RequestMapping("/api/v1/auth")
public record AuthController(AuthService authService) {


    @PostMapping("/login")
    void login(@RequestBody Login request) {
    }

    @PostMapping("/register")
    void register(@RequestBody RegisterUser request) {
    }

    @PostMapping("/refresh")
    void refresh(@RequestBody RefreshToken request) {
    }
}
