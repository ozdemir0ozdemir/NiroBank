package ozdemir0ozdemir.nirobank.tokenservice.api;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ozdemir0ozdemir.nirobank.tokenservice.bridge.Token;
import ozdemir0ozdemir.nirobank.tokenservice.domain.TokenService;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
class TokenController {

    private final TokenService tokenService;

    @PostMapping("/login")
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

    @GetMapping("/{username}")
    ResponseEntity<Token> getExistingTokenFor(@PathVariable String username) {

        Token token = this.tokenService
                .getExistingTokenFor(username);

        return ResponseEntity.ok(token);
    }
}
