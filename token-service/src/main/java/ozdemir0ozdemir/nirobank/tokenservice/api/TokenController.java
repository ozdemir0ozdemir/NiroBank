package ozdemir0ozdemir.nirobank.tokenservice.api;

import org.springframework.web.bind.annotation.*;
import ozdemir0ozdemir.nirobank.tokenservice.domain.TokenStatus;
import ozdemir0ozdemir.nirobank.tokenservice.request.CreateToken;
import ozdemir0ozdemir.nirobank.tokenservice.request.RefreshToken;

@RestController
@RequestMapping("/api/v1/tokens")
record TokenController() {

    @PostMapping
    void login(@RequestBody CreateToken request) {
    }

    @GetMapping("/refresh")
    void refreshAccessToken(@RequestBody RefreshToken request) {
    }

    @GetMapping
    void findTokens(@RequestParam(name = "username", required = false) String username,
                    @RequestParam(name = "token-status", required = false) TokenStatus tokenStatus,
                    @RequestParam(name = "page-number", defaultValue = "0") int pageNumber,
                    @RequestParam(name = "page-size", defaultValue = "10") int pageSize) {

        int page = Math.max(0, pageNumber - 1);
        int size = Math.min(Math.max(5, pageSize), 50);
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