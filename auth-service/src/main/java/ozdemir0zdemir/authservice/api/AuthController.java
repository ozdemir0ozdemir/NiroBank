package ozdemir0zdemir.authservice.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.nirobank.client.tokenclient.AccessToken;
import ozdemir0ozdemir.nirobank.client.tokenclient.request.RefreshToken;
import ozdemir0ozdemir.nirobank.client.userclient.request.Login;
import ozdemir0ozdemir.nirobank.client.userclient.request.RegisterUser;
import ozdemir0zdemir.authservice.domain.AuthService;

// TODO: Implement request models
@RestController
@RequestMapping("/api/v1/auth")
public record AuthController(AuthService authService) {


    @PostMapping("/login")
    ResponseEntity<Response<AccessToken>> login(@RequestBody Login request) {
        return ResponseEntity
                .ok(this.authService.login(request));
    }

    @PostMapping("/register")
    ResponseEntity<Response<Void>> register(@RequestBody RegisterUser request) {
        return ResponseEntity
                .ok( this.authService.register(request.username(), request.password()));
    }

    @PostMapping("/refresh")
    ResponseEntity<Response<AccessToken>> refresh(@RequestBody RefreshToken request) {
        return ResponseEntity
                .ok(this.authService.refresh(request.tokenId()));
    }
}
