package ozdemir0ozdemir.nirobank.authservice.api;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ozdemir0ozdemir.nirobank.common.response.Response;
import ozdemir0ozdemir.nirobank.client.tokenclient.AccessToken;
import ozdemir0ozdemir.nirobank.client.userclient.request.Login;
import ozdemir0ozdemir.nirobank.client.userclient.request.RegisterUser;
import ozdemir0ozdemir.nirobank.authservice.domain.AuthService;
import ozdemir0ozdemir.nirobank.authservice.request.RefreshAccessToken;

@RestController
@RequestMapping("/api/v1/auth")
public record AuthController(AuthService authService) {


    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @PostMapping("/login")
    ResponseEntity<Response<AccessToken>> login(@RequestBody Login request) {
        return ResponseEntity.ok(authService.login(request.username(), request.password()));
    }

    @PostMapping("/register")
    ResponseEntity<Response<Void>> register(@RequestBody RegisterUser request) {
        return ResponseEntity.ok(authService.register(request.username(), request.password()));
    }

    @GetMapping("/refresh")
    ResponseEntity<Response<AccessToken>> refresh(@Valid @RequestBody RefreshAccessToken refreshAccessToken) {
        return ResponseEntity.ok(authService.refresh(refreshAccessToken.refreshTokenReferenceId()));
    }
}
