package ozdemir0ozdemir.nirobank.tokenservice.api;

import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ozdemir0ozdemir.common.response.PagedResponse;
import ozdemir0ozdemir.common.response.Response;
import ozdemir0ozdemir.nirobank.tokenservice.domain.*;
import ozdemir0ozdemir.nirobank.tokenservice.request.GenerateToken;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/tokens")
@RequiredArgsConstructor
@Validated
class TokenController {

    private static final Logger log = LoggerFactory.getLogger(TokenController.class);
    private final RefreshTokenService refreshTokenService;

    @PostMapping
    ResponseEntity<Response<AccessToken>> generateToken(@Valid @RequestBody GenerateToken request) {
        AccessToken result = refreshTokenService.generateTokenFor(request.username(), request.role());

        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{refreshTokenReferenceId}/refresh")
                .buildAndExpand(result.refreshTokenReferenceId())
                .toUri();

        return ResponseEntity.created(location).body(Response.succeeded(result, "Token pair successfully generated"));
    }

    @GetMapping
    ResponseEntity<PagedResponse<RefreshToken>> findTokens(
            @Min(value = 0, message = "Page cannot be less than zero")
            @RequestParam(name = "page", defaultValue = "0") int page,

            @Min(value = 5, message = "Page size cannot be less than five")
            @Max(value = 50, message = "Page size cannot be greater than fifty")
            @RequestParam(name = "size", defaultValue = "10") int size,

            @Nullable
            @Size(min = 4, max = 30, message = "Username length must be in range 4-30")
            @RequestParam(name = "username", required = false) String username,

            @NullableValidRefreshTokenStatus
            @RequestParam(name = "token-status", required = false) String refreshTokenStatus
    ) {

        int pageNumber = Math.max(0, page - 1);
        int pageSize = Math.min(Math.max(5, size), 50);

        Page<RefreshToken> refreshTokenPage;
        if (username == null && refreshTokenStatus == null) {
            refreshTokenPage = refreshTokenService.findAll(page, pageSize);
        }
        else if (username == null) {
            refreshTokenPage = refreshTokenService.findAllByTokenStatus(
                    RefreshTokenStatus.valueOf(refreshTokenStatus),
                    page,
                    pageSize);
        }
        else if (refreshTokenStatus == null) {
            refreshTokenPage = refreshTokenService.findAllByUsername(username, page, pageSize);
        }
        else {
            refreshTokenPage = refreshTokenService.findAllByUsernameAndRefreshTokenStatus(
                    username,
                    RefreshTokenStatus.valueOf(refreshTokenStatus),
                    page,
                    pageSize);
        }

        return ResponseEntity.ok(PagedResponse.succeeded(
                refreshTokenPage,
                refreshTokenPage.getTotalElements() + " refresh token(s) found"));
    }

    @PostMapping("/{refreshTokenReferenceId}/refresh")
    ResponseEntity<Response<AccessToken>> refreshAccessToken(
            @Valid
            @NotBlank(message = "Refresh token reference id cannot be blank or null")
            @PathVariable String refreshTokenReferenceId) {

        Response<AccessToken> response = Response.succeeded(
                refreshTokenService.refreshTokenFor(refreshTokenReferenceId),
                "Access token refreshed");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{refreshTokenReferenceId}")
    ResponseEntity<Response<RefreshToken>> findRefreshTokenByReferenceId(
            @Valid
            @NotBlank(message = "Refresh token reference id cannot be blank or null")
            @PathVariable String refreshTokenReferenceId) {
        RefreshToken refreshToken =
                refreshTokenService.findRefreshTokenByReferenceId(refreshTokenReferenceId);

        return ResponseEntity.ok(Response.succeeded(refreshToken, "Refresh token found"));
    }

}