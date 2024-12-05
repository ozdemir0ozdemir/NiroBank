package ozdemir0ozdemir.nirobank.tokenservice.api;

import org.springframework.web.bind.annotation.*;
import ozdemir0ozdemir.nirobank.tokenservice.domain.TokenStatus;
import ozdemir0ozdemir.nirobank.tokenservice.request.CreateToken;

@RestController
@RequestMapping("/api/v1/tokens")
record JwtController() {

    @PostMapping
    void createTokenFor(@RequestBody CreateToken request) {
        // If the user has a non-expired access token then return it

        // Generate access token

        // Set old access token TokenStatus to REVOKED

        // Save token to repository (in memory cache) formatting a JwtToken
    }

    @GetMapping
    void findTokens(@RequestParam(name = "username", required = false) String username,
                    @RequestParam(name = "token-status", required = false) TokenStatus tokenStatus) {
    }

    @GetMapping("/{tokenId}")
    void getTokenByTokenId(@PathVariable String tokenId) {

    }

    @PostMapping("/{tokenId}")
    void revokeTokenByTokenId(@PathVariable String tokenId) {

    }

    @DeleteMapping("/{tokenId}")
    void deleteTokenByTokenId(@PathVariable String tokenId) {

    }


}