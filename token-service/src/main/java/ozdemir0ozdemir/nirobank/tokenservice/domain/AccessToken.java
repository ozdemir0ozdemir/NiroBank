package ozdemir0ozdemir.nirobank.tokenservice.domain;

import java.util.Date;

public record AccessToken(String refreshTokenId,
                          String accessToken,
                          String username,
                          Date expiresAt,
                          RefreshTokenStatus tokenStatus) {}
