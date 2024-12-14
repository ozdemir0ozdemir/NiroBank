package ozdemir0ozdemir.nirobank.tokenservice.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ozdemir0ozdemir.common.response.PagedResponse;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.nirobank.tokenservice.domain.AccessToken;
import ozdemir0ozdemir.nirobank.tokenservice.domain.RefreshToken;
import ozdemir0ozdemir.nirobank.tokenservice.domain.RefreshTokenService;
import ozdemir0ozdemir.nirobank.tokenservice.domain.RefreshTokenStatus;
import ozdemir0ozdemir.nirobank.tokenservice.request.GenerateToken;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tokens")
record TokenController(RefreshTokenService refreshTokenService) {

    private static final Logger log = LoggerFactory.getLogger(TokenController.class);

    @PostMapping
    ResponseEntity<Response<AccessToken>> generateToken(@RequestBody GenerateToken request) {
        AccessToken result = refreshTokenService.generateTokenFor(request.username(), request.role());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{refreshTokenReferenceId}/refresh")
                .buildAndExpand(result.refreshTokenReferenceId())
                .toUri();

        return ResponseEntity.created(location).body(Response.succeeded(result, "Token pair successfully generated"));
    }

    @GetMapping
    ResponseEntity<PagedResponse<RefreshToken>> findTokens(@RequestParam(name = "username", required = false) String username,
                                                           @RequestParam(name = "token-status", required = false) RefreshTokenStatus refreshTokenStatus,
                                                           @RequestParam(name = "page-number", defaultValue = "0") int pageNumber,
                                                           @RequestParam(name = "page-size", defaultValue = "10") int pageSize) {

        int page = Math.max(0, pageNumber - 1);
        int size = Math.min(Math.max(5, pageSize), 50);

        Page<RefreshToken> refreshTokenPage;
        if (username == null && refreshTokenStatus == null) {
            refreshTokenPage = refreshTokenService.findAll(page, size);
        }
        else if (username == null) {
            refreshTokenPage = refreshTokenService.findAllByTokenStatus(refreshTokenStatus, page, size);
        }
        else if (refreshTokenStatus == null) {
            refreshTokenPage = refreshTokenService.findAllByUsername(username, page, size);
        }
        else {
            refreshTokenPage = refreshTokenService.findAllByUsernameAndRefreshTokenStatus(username, refreshTokenStatus, page, size);
        }

        return ResponseEntity.ok(PagedResponse.succeeded(
                refreshTokenPage,
                refreshTokenPage.getTotalElements() + " refresh token(s) found"));
    }

    @PostMapping("/{refreshTokenReferenceId}/refresh")
    ResponseEntity<Response<AccessToken>> refreshAccessToken(@PathVariable String refreshTokenReferenceId) {

        Response<AccessToken> response = Response.succeeded(
                refreshTokenService.refreshTokenFor(refreshTokenReferenceId),
                "Access token refreshed");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{refreshTokenReferenceId}")
    ResponseEntity<Response<RefreshToken>> findRefreshTokenByReferenceId(@PathVariable String refreshTokenReferenceId) {
        RefreshToken refreshToken =
                refreshTokenService.findRefreshTokenByReferenceId(refreshTokenReferenceId);

        return ResponseEntity.ok(Response.succeeded(refreshToken, "Refresh token found"));
    }

}