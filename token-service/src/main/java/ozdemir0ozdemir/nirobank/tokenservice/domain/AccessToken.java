package ozdemir0ozdemir.nirobank.tokenservice.domain;

import java.sql.Timestamp;

public record AccessToken(String refreshTokenReferenceId,
                          String username,
                          String accessToken,
                          Timestamp expiresAt) {
}
