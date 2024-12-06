package ozdemir0ozdemir.nirobank.tokenservice.domain;

import java.time.LocalDateTime;

public record AccessToken(String refreshTokenId,
                          String accessToken,
                          String username,
                          LocalDateTime expiresAt,
                          TokenStatus tokenStatus) {}
