package ozdemir0ozdemir.nirobank.tokenservice.api;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ozdemir0ozdemir.common.response.PagedResponse;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.nirobank.tokenservice.domain.AccessToken;
import ozdemir0ozdemir.nirobank.tokenservice.domain.Token;
import ozdemir0ozdemir.nirobank.tokenservice.domain.TokenService;
import ozdemir0ozdemir.nirobank.tokenservice.domain.TokenStatus;
import ozdemir0ozdemir.nirobank.tokenservice.request.CreateToken;
import ozdemir0ozdemir.nirobank.tokenservice.request.RefreshToken;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tokens")
record TokenController(TokenService service) {

    @PostMapping
    ResponseEntity<Response<AccessToken>> createToken(@RequestBody CreateToken request) {
        AccessToken accessToken = this.service
                .createTokens(request.username(), request.role().getPermissions());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/refresh/{refreshTokenId}")
                .buildAndExpand(accessToken.refreshTokenId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(Response.succeeded(accessToken, "Tokens successfully created"));
    }

    @GetMapping("/refresh")
    ResponseEntity<Response<AccessToken>> refreshAccessToken(@RequestBody RefreshToken request) {
        AccessToken accessToken = this.service.refreshToken(request.tokenId());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/refresh/{refreshTokenId}")
                .buildAndExpand(accessToken.refreshTokenId())
                .toUri();

        return ResponseEntity
                .created(location)
                .body(Response.succeeded(accessToken, "Tokens successfully refreshed"));
    }

    @GetMapping
    ResponseEntity<PagedResponse<Token>> findTokens(@RequestParam(name = "username", required = false) String username,
                                                    @RequestParam(name = "token-status", required = false) TokenStatus tokenStatus,
                                                    @RequestParam(name = "page-number", defaultValue = "0") int pageNumber,
                                                    @RequestParam(name = "page-size", defaultValue = "10") int pageSize) {

        int page = Math.max(0, pageNumber - 1);
        int size = Math.min(Math.max(5, pageSize), 50);

        boolean usernameValid = username != null && !username.isBlank();
        boolean tokenStatusValid = tokenStatus != null;

        Page<Token> tokenPage;
        if (!usernameValid && !tokenStatusValid) {
            tokenPage = this.service.findAll(page, size);
        }
        else if (!usernameValid) {
            tokenPage = this.service.findAllByTokenStatus(tokenStatus, page, size);
        }
        else if (!tokenStatusValid) {
            tokenPage = this.service.findAllByUsername(username, page, size);
        }
        else {
            tokenPage = this.service.findAllByUsernameAndTokenStatus(username, tokenStatus, page, size);
        }

        PagedResponse<Token> response = PagedResponse.succeeded(tokenPage, "Request succeeded");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{tokenId}")
    ResponseEntity<Response<Token>> getTokenByTokenId(@PathVariable String tokenId) {
        Token token = this.service.findByTokenId(tokenId);

        return ResponseEntity.ok(Response.succeeded(token, "Token found"));
    }

    @PostMapping("/{tokenId}")
    ResponseEntity<Void> revokeTokenByTokenId(@PathVariable String tokenId) {
        this.service.revokeTokenByTokenId(tokenId);

        return ResponseEntity.noContent().build();

    }

    @DeleteMapping("/{tokenId}")
    ResponseEntity<Void> deleteTokenByTokenId(@PathVariable String tokenId) {
        this.service.deleteTokenByTokenId(tokenId);

        return ResponseEntity.noContent().build();
    }


}