package ozdemir0ozdemir.nirobank.authserver.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ozdemir0ozdemir.nirobank.authserver.model.Token;
import ozdemir0ozdemir.nirobank.authserver.request.GenerateAccessToken;
import ozdemir0ozdemir.nirobank.authserver.request.RefreshAccessToken;
import ozdemir0ozdemir.nirobank.authserver.service.TokenService;

@RestController
@RequestMapping("/api/v1/auth/token")
@RequiredArgsConstructor
class TokenController {

    private final TokenService tokenService;

    @PostMapping("/access")
    ResponseEntity<Token> generateAccessToken(@RequestBody GenerateAccessToken request) {
       Token token = this.tokenService
               .generateTokenSet(request.username());

       return ResponseEntity.ok(token);
    }

    @PostMapping("/refresh")
    ResponseEntity<?> refreshAccessToken(@RequestBody RefreshAccessToken request) {

        Token token = this.tokenService
                .refreshOrGetTokenSet(request.username(), request.refreshToken());

        return ResponseEntity.ok(token);
    }
}
