package ozdemir0ozdemir.nirobank.tokenservice.domain;

import java.time.Instant;

public record AccessToken(String refreshTokenId,
                          String accessToken,
                          String username,
                          Instant expiresAt,
                          TokenStatus tokenStatus) {}
