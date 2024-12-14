package ozdemir0ozdemir.nirobank.client.tokenclient;

import java.sql.Timestamp;

public record AccessToken(String refreshTokenReferenceId,
                          String username,
                          String accessToken,
                          Timestamp expiresAt) {
}
