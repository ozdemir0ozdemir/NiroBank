package ozdemir0ozdemir.nirobank.authservice.request;

import jakarta.validation.constraints.NotBlank;

public record RefreshAccessToken(
        @NotBlank(message = "Refresh token reference id cannot be blank or null")
        String refreshTokenReferenceId) {
}
