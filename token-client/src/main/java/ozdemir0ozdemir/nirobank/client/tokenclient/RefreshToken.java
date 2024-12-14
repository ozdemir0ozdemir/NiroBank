package ozdemir0ozdemir.nirobank.client.tokenclient;

import java.sql.Timestamp;

public record RefreshToken(Long id,
                           String referenceId,
                           String username,
                           String refreshToken,
                           Timestamp expiresAt,
                           RefreshTokenStatus refreshTokenStatus) {
}
