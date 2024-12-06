package ozdemir0ozdemir.nirobank.tokenservice.api;

import org.springframework.web.bind.annotation.*;
import ozdemir0ozdemir.nirobank.tokenservice.domain.TokenStatus;
import ozdemir0ozdemir.nirobank.tokenservice.request.CreateToken;
import ozdemir0ozdemir.nirobank.tokenservice.request.RefreshToken;

@RestController
@RequestMapping("/api/v1/tokens")
record TokenController() {

    @PostMapping
    void createTokenFor(@RequestBody CreateToken request) {
        // If user has a refresh token already then revoke it

        // Generate Access and Refresh Token
        // Store refresh token in postgres
        // Return access token and refresh token id to the client
    }

    @GetMapping("/refresh")
    void refreshAccessToken(@RequestBody RefreshToken request) {

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