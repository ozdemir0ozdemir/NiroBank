package ozdemir0ozdemir.nirobank.tokenservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ozdemir0ozdemir.nirobank.tokenservice.model.Token;
import ozdemir0ozdemir.nirobank.tokenservice.request.GenerateAccessToken;
import ozdemir0ozdemir.nirobank.tokenservice.request.RefreshAccessToken;
import ozdemir0ozdemir.nirobank.tokenservice.service.TokenService;

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
    ResponseEntity<Token> refreshAccessToken(@RequestBody RefreshAccessToken request) {

        Token token = this.tokenService
                .refreshOrGetTokenSet(request.username(), request.refreshToken());

        return ResponseEntity.ok(token);
    }
}
