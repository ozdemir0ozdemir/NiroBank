package ozdemir0ozdemir.nirobank.tokenservice.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ozdemir0ozdemir.common.response.PagedResponse;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.nirobank.tokenservice.domain.AccessToken;
import ozdemir0ozdemir.nirobank.tokenservice.domain.Token;
import ozdemir0ozdemir.nirobank.tokenservice.domain.RefreshTokenStatus;
import ozdemir0ozdemir.nirobank.tokenservice.request.CreateToken;
import ozdemir0ozdemir.nirobank.tokenservice.request.RefreshToken;

@RestController
@RequestMapping("/api/v1/tokens")
record TokenController() {

    private static final Logger log = LoggerFactory.getLogger(TokenController.class);

    @PostMapping
    ResponseEntity<Response<AccessToken>> createToken(@RequestBody CreateToken request) {
        return null;
    }

    @PostMapping("/refresh")
    ResponseEntity<Response<AccessToken>> refreshAccessToken(@RequestBody RefreshToken request) {
        return null;
    }

    @GetMapping
    ResponseEntity<PagedResponse<Token>> findTokens(@RequestParam(name = "username", required = false) String username,
                                                    @RequestParam(name = "token-status", required = false) RefreshTokenStatus tokenStatus,
                                                    @RequestParam(name = "page-number", defaultValue = "0") int pageNumber,
                                                    @RequestParam(name = "page-size", defaultValue = "10") int pageSize) {

        int page = Math.max(0, pageNumber - 1);
        int size = Math.min(Math.max(5, pageSize), 50);

        boolean usernameValid = username != null && !username.isBlank();
        boolean tokenStatusValid = tokenStatus != null;

        Page<Token> tokenPage;
        if (!usernameValid && !tokenStatusValid) {
        }
        else if (!usernameValid) {
        }
        else if (!tokenStatusValid) {
        }
        else {
        }
        return null;
    }

    @GetMapping("/{tokenId}")
    ResponseEntity<Response<Token>> getTokenByTokenId(@PathVariable String tokenId) {
        return null;
    }

    @PostMapping("/{tokenId}")
    ResponseEntity<Void> revokeTokenByTokenId(@PathVariable String tokenId) {
        return null;
    }

    @DeleteMapping("/{tokenId}")
    ResponseEntity<Void> deleteTokenByTokenId(@PathVariable String tokenId) {
        return null;
    }

}